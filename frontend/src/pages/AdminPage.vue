<template>
  <div class="admin-container">
    <h1 class="page-title">Admin Control Panel</h1>
    
    <div class="admin-section">
      <h2>User Management</h2>
      
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>Loading user list...</p>
      </div>
      
      <div v-else-if="error" class="error-message">
        {{ error }}
      </div>
      
      <div v-else class="user-management">
        <div class="filter-controls">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="Search username or email..." 
            class="search-input"
          />
          <select v-model="roleFilter" class="role-filter">
            <option value="">All Roles</option>
            <option value="ADMIN">Admin</option>
            <option value="MEMBER">Member</option>
            <option value="USER">User</option>
          </select>
        </div>
        
        <div class="user-stats">
          <div class="stat-item">
            <span class="stat-number">{{ totalUsers }}</span>
            <span class="stat-label">Total Users</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ adminCount }}</span>
            <span class="stat-label">Admins</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ memberCount }}</span>
            <span class="stat-label">Members</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ userCount }}</span>
            <span class="stat-label">Users</span>
          </div>
        </div>
        
        <div class="user-table-container">
          <table class="user-table">
            <thead>
              <tr>
                <th>Avatar</th>
                <th>Username</th>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Registration Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="user in filteredUsers" :key="user.id" class="user-row">
                <td>
                  <img :src="user.avatar" :alt="user.username" class="user-avatar" />
                </td>
                <td>{{ user.username }}</td>
                <td>{{ user.firstName }} {{ user.lastName }}</td>
                <td>{{ user.email }}</td>
                <td>
                  <span :class="['role-badge', `role-${user.role.toLowerCase()}`]">
                    {{ getUserRoleLabel(user.role) }}
                  </span>
                </td>
                <td>{{ formatDate(user.createdAt || new Date()) }}</td>
                <td>
                  <div v-if="user.role !== 'ADMIN'" class="action-buttons">
                    <button 
                      v-if="user.role === 'USER'" 
                      @click="promoteToMember(user)" 
                      class="btn btn-promote"
                      :disabled="promotingUsers.includes(user.id)"
                    >
                      <span v-if="promotingUsers.includes(user.id)">Processing...</span>
                      <span v-else>Promote to Member</span>
                    </button>
                    <button 
                      v-if="user.role === 'MEMBER'" 
                      @click="demoteToUser(user)" 
                      class="btn btn-demote"
                      :disabled="demotingUsers.includes(user.id)"
                    >
                      <span v-if="demotingUsers.includes(user.id)">Processing...</span>
                      <span v-else>Demote to User</span>
                    </button>
                  </div>
                  <span v-else class="no-action">Cannot modify</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <div v-if="filteredUsers.length === 0" class="no-results">
          No matching users found
        </div>
      </div>
    </div>
    
    <!-- Comments Management Section -->
    <div class="admin-section">
      <h2>Comments Management</h2>
      
      <div v-if="commentsLoading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>Loading comments...</p>
      </div>
      
      <div v-else-if="commentsError" class="error-message">
        {{ commentsError }}
      </div>
      
      <div v-else class="comments-management">
        <div class="filter-controls">
          <input 
            type="text" 
            v-model="commentSearchQuery" 
            placeholder="Search comment content..." 
            class="search-input"
          />
          <select v-model="commentNewsFilter" class="role-filter">
            <option value="">All News</option>
            <option v-for="news in newsOptions" :key="news.id" :value="news.id">
              {{ news.title }}
            </option>
          </select>
        </div>
        
        <div class="comments-stats">
          <div class="stat-item">
            <span class="stat-number">{{ totalComments }}</span>
            <span class="stat-label">Total Comments</span>
          </div>
        </div>
        
        <div class="table-container">
          <table class="admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Content</th>
                <th>Author</th>
                <th>News</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="comment in filteredComments" :key="comment.id">
                <td>{{ comment.id }}</td>
                <td class="comment-content">{{ truncateText(comment.content, 100) }}</td>
                <td>{{ comment.author?.username || 'Anonymous' }}</td>
                <td>{{ comment.news?.title || 'Unknown News' }}</td>
                <td>{{ formatDate(comment.createdAt) }}</td>
                <td>
                  <button 
                    @click="deleteComment(comment)" 
                    class="btn btn-delete"
                    :disabled="deletingComments.includes(comment.id)"
                  >
                    <span v-if="deletingComments.includes(comment.id)">Deleting...</span>
                    <span v-else>Delete</span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          
          <div v-if="filteredComments.length === 0" class="no-results">
            No comments found
          </div>
        </div>
      </div>
    </div>
    
    <!-- News Management Section -->
    <div class="admin-section">
      <h2>News Management</h2>
      
      <div v-if="newsLoading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>Loading news...</p>
      </div>
      
      <div v-else-if="newsError" class="error-message">
        {{ newsError }}
      </div>
      
      <div v-else class="news-management">
        <div class="filter-controls">
          <input 
            type="text" 
            v-model="newsSearchQuery" 
            placeholder="Search news title..." 
            class="search-input"
          />
          <select v-model="newsCategoryFilter" class="role-filter">
            <option value="">All Categories</option>
            <option v-for="category in newsCategories" :key="category" :value="category">
              {{ category }}
            </option>
          </select>
        </div>
        
        <div class="comments-stats">
          <div class="stat-item">
            <span class="stat-number">{{ totalNews }}</span>
            <span class="stat-label">Total News</span>
          </div>
        </div>
        
        <div class="table-container">
          <table class="admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Category</th>
                <th>Author</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="news in filteredNews" :key="news.id">
                <td>{{ news.id }}</td>
                <td class="news-title">{{ truncateText(news.title, 100) }}</td>
                <td>
                  <span class="category-badge">{{ news.category }}</span>
                </td>
                <td>{{ news.author?.username || 'Admin' }}</td>
                <td>{{ formatDate(news.publishDate) }}</td>
                <td>
                  <button 
                    @click="deleteNews(news)" 
                    class="btn btn-delete"
                    :disabled="deletingNews.includes(news.id)"
                  >
                    <span v-if="deletingNews.includes(news.id)">Deleting...</span>
                    <span v-else>Delete</span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          
          <div v-if="filteredNews.length === 0" class="no-results">
            No news found
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import apiService from '../api/apiService'

export default {
  name: 'AdminPage',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    
    const loading = ref(false)
    const error = ref('')
    const users = ref([])
    const searchQuery = ref('')
    const roleFilter = ref('')
    const promotingUsers = ref([])
    const demotingUsers = ref([])
    
    // Comments Management
  const comments = ref([])
  const commentsLoading = ref(false)
  const commentsError = ref(null)
  const commentSearchQuery = ref('')
  const commentNewsFilter = ref('')
  const newsOptions = ref([])
  const deletingComments = ref([])
  
  // News Management
  const newsItems = ref([])
  const newsLoading = ref(false)
  const newsError = ref(null)
  const newsSearchQuery = ref('')
  const newsCategoryFilter = ref('')
  const newsCategories = ref(['Technology', 'Economy', 'Entertainment', 'Health', 'Politics'])
  const deletingNews = ref([])
    
    // Check permissions, ensure only admin can access
    const checkAdminPermission = () => {
      if (!authStore.isAuthenticated || !authStore.isAdmin) {
        router.push('/login')
        return false
      }
      return true
    }
    
    // Fetch user list
    const fetchUsers = async () => {
      loading.value = true
      error.value = ''
      
      try {
        // In a real application, we would call the API to get user list
      // const response = await api.user.getAll()
      // users.value = response.data
      
      // Simulate fetching user data (from localStorage)
      await new Promise(resolve => setTimeout(resolve, 500)) // Simulate network delay
      
      // Read all users from localStorage (in real app should fetch from API)
        const storedUsers = localStorage.getItem('allUsers')
        if (storedUsers) {
          users.value = JSON.parse(storedUsers)
        } else {
          // Generate mock user data
          users.value = generateMockUsers()
        }
      } catch (err) {
        error.value = 'Failed to fetch user list'
        console.error('Error fetching users:', err)
      } finally {
        loading.value = false
      }
    }
    
    // Generate mock comment data
  const loadMockComments = () => {
    const mockComments = [
      {
        id: 1,
        content: 'This is a test comment with simple content.',
        author: { id: 101, username: 'user1' },
        news: { id: 1, title: 'Latest Tech Breakthrough: How AI is Changing Our Lives' },
        likes: 5,
        dislikes: 1,
        createdAt: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000) // 2 days ago
      },
      {
        id: 2,
        content: 'This news is really interesting, I learned a lot of new knowledge. Thanks for sharing!',
        author: { id: 102, username: 'user2' },
        news: { id: 1, title: 'Latest Tech Breakthrough: How AI is Changing Our Lives' },
        likes: 3,
        dislikes: 0,
        createdAt: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000) // 1 day ago
      },
      {
        id: 3,
        content: 'I disagree with the article\'s perspective. In some aspects, this technology might bring negative impacts.',
        author: { id: 103, username: 'member1' },
        news: { id: 2, title: 'Expert Prediction: Market Trend Analysis for the Next Five Years' },
        likes: 1,
        dislikes: 2,
        createdAt: new Date(Date.now() - 12 * 60 * 60 * 1000) // 12 hours ago
      },
      {
        id: 4,
        content: 'Please provide more information on this topic. I would like to learn more details about it.',
        author: { id: 104, username: 'user1' },
        news: { id: 3, title: 'Global Climate Change: Latest Research Findings' },
        likes: 0,
        dislikes: 0,
        createdAt: new Date(Date.now() - 6 * 60 * 60 * 1000) // 6 hours ago
      }
    ]
    
    const mockNews = [
      { id: 1, title: 'Latest Tech Breakthrough: How AI is Changing Our Lives' },
      { id: 2, title: 'Expert Prediction: Market Trend Analysis for the Next Five Years' },
      { id: 3, title: 'Global Climate Change: Latest Research Findings' },
      { id: 4, title: 'Health Tips for Better Living in the New Year' }
    ]
    
    comments.value = mockComments
    newsOptions.value = mockNews
  }
  
  // Mock news data for demo
  const loadMockNews = () => {
    const mockNews = [
      {
        id: 1,
        title: 'Breaking News: Tech Innovation Revolutionizes Artificial Intelligence',
        content: 'A groundbreaking technology has emerged that promises to transform the AI landscape...',
        category: 'Technology',
        author: { id: 1, username: 'admin' },
        publishDate: new Date('2024-01-15T09:00:00'),
        views: 1245,
        likes: 87
      },
      {
        id: 2,
        title: 'Economic Trends in 2024: What to Expect',
        content: 'Financial experts predict significant changes in global markets this year...',
        category: 'Economy',
        author: { id: 2, username: 'finance_expert' },
        publishDate: new Date('2024-01-14T14:30:00'),
        views: 892,
        likes: 45
      },
      {
        id: 3,
        title: 'Celebrity Rumors Debunked: Setting the Record Straight',
        content: 'Social media has been abuzz with false information about several celebrities...',
        category: 'Entertainment',
        author: { id: 3, username: 'entertainment_news' },
        publishDate: new Date('2024-01-13T11:15:00'),
        views: 2340,
        likes: 156
      },
      {
        id: 4,
        title: 'Health Tips for Better Living in the New Year',
        content: 'Health professionals share their top recommendations for improving wellness...',
        category: 'Health',
        author: { id: 4, username: 'health_expert' },
        publishDate: new Date('2024-01-12T08:45:00'),
        views: 1678,
        likes: 112
      },
      {
        id: 5,
        title: 'Political Analysis: Key Issues Shaping the Upcoming Election',
        content: 'As election season approaches, several critical topics are emerging...',
        category: 'Politics',
        author: { id: 5, username: 'political_analyst' },
        publishDate: new Date('2024-01-11T16:20:00'),
        views: 1456,
        likes: 78
      }
    ]
    
    newsItems.value = mockNews
  }
    
    const fetchComments = async () => {
    commentsLoading.value = true
    commentsError.value = null
    
    try {
      // In a real app, we would fetch comments from API
      // For demo purposes, we'll use mock data
      await new Promise(resolve => setTimeout(resolve, 500)) // Simulate API delay
      loadMockComments()
    } catch (err) {
      commentsError.value = 'Failed to fetch comments'
      console.error('Error fetching comments:', err)
      // Fallback to mock data on error
      loadMockComments()
    } finally {
      commentsLoading.value = false
    }
  }
  
  const fetchNews = async () => {
    newsLoading.value = true
    newsError.value = null
    
    try {
      // In a real app, we would fetch news from API
      // For demo purposes, we'll use mock data
      await new Promise(resolve => setTimeout(resolve, 500)) // Simulate API delay
      loadMockNews()
    } catch (err) {
      newsError.value = 'Failed to fetch news'
      console.error('Error fetching news:', err)
      // Fallback to mock data on error
      loadMockNews()
    } finally {
      newsLoading.value = false
    }
  }
    
    const deleteComment = async (comment) => {
    if (confirm(`Are you sure you want to delete this comment?`)) {
      deletingComments.value.push(comment.id)
      
      try {
        // In a real app, we would call the API to delete the comment
        // await apiService.comment.delete(comment.id)
        
        // For demo, simulate API call with timeout
        await new Promise(resolve => setTimeout(resolve, 500))
        
        // Remove from local list
        comments.value = comments.value.filter(c => c.id !== comment.id)
        alert('Comment successfully deleted!')
      } catch (err) {
        console.error('Error deleting comment:', err)
        alert('Failed to delete comment, please try again')
      } finally {
        deletingComments.value = deletingComments.value.filter(id => id !== comment.id)
      }
    }
  }
  
  const deleteNews = async (news) => {
    if (confirm(`Are you sure you want to delete this news article: "${news.title}"?\nThis will also delete all associated comments.`)) {
      deletingNews.value.push(news.id)
      
      try {
        // In a real app, we would call the API to delete the news
        // await apiService.news.delete(news.id)
        
        // For demo, simulate API call with timeout
        await new Promise(resolve => setTimeout(resolve, 500))
        
        // Remove from local list
        newsItems.value = newsItems.value.filter(n => n.id !== news.id)
        
        // Also remove associated comments
        comments.value = comments.value.filter(c => c.news?.id !== news.id)
        
        alert('News article successfully deleted!')
      } catch (err) {
        console.error('Error deleting news:', err)
        alert('Failed to delete news article, please try again')
      } finally {
        deletingNews.value = deletingNews.value.filter(id => id !== news.id)
      }
    }
  }
    
    // Generate mock user data
    const generateMockUsers = () => {
      const mockUsers = [
        {
          id: 1,
          username: 'admin',
          firstName: 'Administrator',
          lastName: 'System',
          email: 'admin@example.com',
          role: 'ADMIN',
          avatar: 'https://picsum.photos/id/1/100/100',
          createdAt: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000) // 30 days ago
        },
        {
          id: 2,
          username: 'member1',
          firstName: 'Test',
          lastName: 'Member1',
          email: 'member1@example.com',
          role: 'MEMBER',
          avatar: 'https://picsum.photos/id/2/100/100',
          createdAt: new Date(Date.now() - 15 * 24 * 60 * 60 * 1000) // 15 days ago
        },
        {
          id: 3,
          username: 'user1',
          firstName: 'Test',
          lastName: 'User1',
          email: 'user1@example.com',
          role: 'USER',
          avatar: 'https://picsum.photos/id/3/100/100',
          createdAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000) // 7 days ago
        },
        {
          id: 4,
          username: 'user2',
          firstName: 'Test',
          lastName: 'User2',
          email: 'user2@example.com',
          role: 'USER',
          avatar: 'https://picsum.photos/id/4/100/100',
          createdAt: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000) // 3 days ago
        }
      ]
      
      // Add current logged-in user to the list if not exists
      if (authStore.user && !mockUsers.find(u => u.id === authStore.user.id)) {
        mockUsers.push({
          ...authStore.user,
          createdAt: new Date()
        })
      }
      
      // Save to localStorage for future use
      localStorage.setItem('allUsers', JSON.stringify(mockUsers))
      
      return mockUsers
    }
    
    // Promote user to member
    const promoteToMember = async (user) => {
      promotingUsers.value.push(user.id)
      
      try {
        // In a real application, we would call the API to update user role
        // await api.user.updateRole(user.id, 'MEMBER')
        
        // Simulate API call delay
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        // Update local user data
        const userIndex = users.value.findIndex(u => u.id === user.id)
        if (userIndex !== -1) {
          users.value[userIndex].role = 'MEMBER'
          // Update localStorage
          localStorage.setItem('allUsers', JSON.stringify(users.value))
          
          // If it's the currently logged-in user, update the role in authStore
          if (authStore.user && authStore.user.id === user.id) {
            const updatedUser = { ...authStore.user, role: 'MEMBER' }
            localStorage.setItem('user', JSON.stringify(updatedUser))
            authStore.user = updatedUser
          }
        }
        
        alert('User successfully promoted to Member!')
      } catch (err) {
        console.error('Error promoting user:', err)
        alert('Failed to promote user, please try again')
      } finally {
        promotingUsers.value = promotingUsers.value.filter(id => id !== user.id)
      }
    }
    
    // Demote user to regular user
    const demoteToUser = async (user) => {
      demotingUsers.value.push(user.id)
      
      try {
        // In a real application, we would call the API to update user role
        // await api.user.updateRole(user.id, 'USER')
        
        // Simulate API call delay
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        // Update local user data
        const userIndex = users.value.findIndex(u => u.id === user.id)
        if (userIndex !== -1) {
          users.value[userIndex].role = 'USER'
          // Update localStorage
          localStorage.setItem('allUsers', JSON.stringify(users.value))
          
          // If it's the currently logged-in user, update the role in authStore
          if (authStore.user && authStore.user.id === user.id) {
            const updatedUser = { ...authStore.user, role: 'USER' }
            localStorage.setItem('user', JSON.stringify(updatedUser))
            authStore.user = updatedUser
          }
        }
        
        alert('User successfully demoted to User!')
      } catch (err) {
        console.error('Error demoting user:', err)
        alert('Failed to demote user, please try again')
      } finally {
        demotingUsers.value = demotingUsers.value.filter(id => id !== user.id)
      }
    }
    
    // Format date
    const formatDate = (date) => {
        const d = new Date(date)
        return d.toLocaleDateString('en-US', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit'
        })
      }
    
    // Truncate text with ellipsis
    const truncateText = (text, maxLength) => {
      if (!text || text.length <= maxLength) return text
      return text.substring(0, maxLength) + '...'
    }
    
    // Text truncation function for news titles
    const truncateTitle = (title, maxLength = 50) => {
      if (!title || title.length <= maxLength) return title
      return title.substring(0, maxLength) + '...'
    }
    
    // Get user role label
    const getUserRoleLabel = (role) => {
      const roleMap = {
        'ADMIN': 'Admin',
        'MEMBER': 'Member',
        'USER': 'User'
      }
      return roleMap[role] || 'Unknown Role'
    }
    
    // Computed property: Filtered user list
    const filteredUsers = computed(() => {
      return users.value.filter(user => {
        // Search filtering
        const matchesSearch = !searchQuery.value || 
          user.username.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
          user.email.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
          `${user.firstName} ${user.lastName}`.toLowerCase().includes(searchQuery.value.toLowerCase())
        
        // Role filtering
        const matchesRole = !roleFilter.value || user.role === roleFilter.value
        
        return matchesSearch && matchesRole
      })
    })
    
    // Comments computed properties
    const totalComments = computed(() => comments.value.length)
    
    const filteredComments = computed(() => {
      return comments.value.filter(comment => {
        const matchesSearch = comment.content.toLowerCase().includes(commentSearchQuery.value.toLowerCase())
        const matchesNews = !commentNewsFilter.value || comment.news?.id === Number(commentNewsFilter.value)
        return matchesSearch && matchesNews
      })
    })
    
    // News computed properties
    const totalNews = computed(() => newsItems.value.length)
    
    const filteredNews = computed(() => {
      let result = [...newsItems.value]
      
      // Filter by search query
      if (newsSearchQuery.value) {
        const query = newsSearchQuery.value.toLowerCase()
        result = result.filter(news => 
          news.title.toLowerCase().includes(query) ||
          (news.author && news.author.username ? news.author.username.toLowerCase().includes(query) : false)
        )
      }
      
      // Filter by category
      if (newsCategoryFilter.value && newsCategoryFilter.value !== 'all') {
        result = result.filter(news => news.category === newsCategoryFilter.value)
      }
      
      return result
    })
    
    // Computed properties: User statistics
    const totalUsers = computed(() => users.value.length)
    const adminCount = computed(() => users.value.filter(u => u.role === 'ADMIN').length)
    const memberCount = computed(() => users.value.filter(u => u.role === 'MEMBER').length)
    const userCount = computed(() => users.value.filter(u => u.role === 'USER').length)
    
    // Execute when page is mounted
  onMounted(() => {
    if (checkAdminPermission()) {
      fetchUsers()
      fetchComments()
      fetchNews()
    }
  })
    
    return {
      loading,
      error,
      users,
      searchQuery,
      roleFilter,
      promotingUsers,
      demotingUsers,
      filteredUsers,
      totalUsers,
      adminCount,
      memberCount,
      userCount,
      promoteToMember,
      demoteToUser,
      formatDate,
      getUserRoleLabel,
      
      // Comments management
      comments,
      commentsLoading,
      commentsError,
      commentSearchQuery,
      commentNewsFilter,
      newsOptions,
      deletingComments,
      filteredComments,
      totalComments,
      fetchComments,
      deleteComment,
      truncateText,
      
      // News management
      newsItems,
      newsLoading,
      newsError,
      newsSearchQuery,
      newsCategoryFilter,
      newsCategories,
      deletingNews,
      filteredNews,
      totalNews,
      fetchNews,
      deleteNews,
      truncateTitle
    }
  }
}
</script>

<style scoped>
.admin-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  color: #333;
  font-size: 28px;
  margin-bottom: 30px;
  text-align: center;
}

.admin-section {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 30px;
}

.admin-section h2 {
  color: #333;
  font-size: 20px;
  margin-bottom: 20px;
  border-bottom: 2px solid #e0e0e0;
  padding-bottom: 10px;
}

.loading-container {
  text-align: center;
  padding: 40px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  margin: 0 auto 15px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-message {
  color: #e74c3c;
  background-color: #fadbd8;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.filter-controls {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.search-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.role-filter {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  min-width: 150px;
}

.user-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.stat-item {
  flex: 1;
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 6px;
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 5px;
}

.stat-label {
  display: block;
  font-size: 14px;
  color: #7f8c8d;
}

.user-table-container {
  overflow-x: auto;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
}

.user-table th,
.user-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.user-table th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.user-row:hover {
  background-color: #f8f9fa;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.role-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.role-admin {
  background-color: #e3f2fd;
  color: #1976d2;
}

.role-member {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.role-user {
  background-color: #fff3e0;
  color: #e65100;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-promote {
  background-color: #27ae60;
  color: white;
}

.btn-promote:hover:not(:disabled) {
  background-color: #229954;
}

.btn-demote {
  background-color: #f39c12;
  color: white;
}

.btn-demote:hover:not(:disabled) {
  background-color: #e67e22;
}

.btn-delete {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-delete:hover:not(:disabled) {
  background-color: #da190b;
}

.comment-content {
  max-width: 300px;
  word-wrap: break-word;
}

.comments-stats {
  display: flex;
  margin: 15px 0;
  gap: 20px;
}

.btn:disabled {
  background-color: #bdc3c7;
  cursor: not-allowed;
}

.no-action {
  font-size: 12px;
  color: #95a5a6;
  font-style: italic;
}

.no-results {
  text-align: center;
  padding: 40px;
  color: #7f8c8d;
}

@media (max-width: 768px) {
  .user-stats {
    flex-wrap: wrap;
  }
  
  .stat-item {
    flex-basis: calc(50% - 10px);
  }
  
  .filter-controls {
    flex-direction: column;
  }
}
</style>