<template>
  <div class="home-page">
    <!-- Welcome Message -->
    <section class="welcome-section">
      <div class="welcome-container">
        <h2 class="welcome-title">
          Welcome, {{ getUserRoleLabel(currentUser?.role) }}!
        </h2>
        <p class="welcome-text">
          Explore the latest news and stay informed with reliable information.
        </p>
      </div>
    </section>
    
    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-container">
        <h1 class="hero-title">Truth Moment</h1>
        <p class="hero-description">
          We are committed to providing you with real and reliable news information through advanced algorithms and professional team reviews,
          helping you identify false information and obtain valuable news content.
        </p>,
        <div class="hero-actions">
          <router-link to="/news/latest" class="btn btn-primary">
            Browse Latest News
          </router-link>
          <router-link to="/about" class="btn btn-secondary">
            Learn About Our Service
          </router-link>
        </div>
      </div>
    </section>

    <!-- Core Features -->
    <section class="features-section">
      <div class="section-header">
        <h2 class="section-title">Our Core Advantages</h2>
      </div>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">🔍</div>
          <h3 class="feature-title">Fact Checking</h3>
          <p class="feature-description">
            Multi-dimensional verification of news authenticity, providing reliable credibility scores through a combination of AI technology and human review.
          </p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">⚡</div>
          <h3 class="feature-title">Real-time Updates</h3>
          <p class="feature-description">
            24/7 monitoring of global news dynamics to ensure you get the latest and most accurate information first.
          </p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">📊</div>
          <h3 class="feature-title">Data Visualization</h3>
          <p class="feature-description">
            Intuitively display news credibility indicators through charts and rating systems, allowing you to easily judge information reliability.
          </p>
        </div>
      </div>
    </section>
    
    <!-- Category Tabs -->
    <CategoryTabs :newsCounts="newsCounts" />
    
    <!-- Loading State -->
    <div v-if="isLoading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>Loading news...</p>
    </div>
    
    <!-- Error State -->
    <div v-else-if="error" class="error-container">
      <div class="error-icon">⚠️</div>
      <h3>Failed to Load</h3>
      <p>{{ error }}</p>
      <button class="btn btn-secondary" @click="fetchNews">Retry</button>
    </div>
    
    <!-- Content Area -->
    <div v-else>
      <!-- Featured News Section -->
      <section v-if="featuredNews.length > 0" class="featured-section">
        <div class="section-header">
          <h2 class="section-title">Featured News</h2>
          <router-link to="/featured" class="section-action">
            View All
            <span>→</span>
          </router-link>
        </div>
        <div class="featured-grid">
          <div v-for="news in featuredNews" :key="news.id" class="featured-card">
            <router-link :to="`/news/${news.id}`" class="featured-link">
              <div class="featured-image-wrapper">
                <img :src="news.imageUrl || 'https://picsum.photos/id/'+(news.id%100)+'/800/450'" :alt="news.title" class="featured-image" />
                <!-- Trust Indicator -->
                <div class="trust-indicator" :class="getTrustLevelClass(news.trustScore)">
                  <div class="trust-icon">{{ getTrustLevelIcon(news.trustScore) }}</div>
                  <div class="trust-text">{{ news.trustScore }}%</div>
                </div>
                <!-- Trust Progress Bar -->
                <div class="trust-progress-bar-container">
                  <div 
                    class="trust-progress-bar" 
                    :class="getTrustLevelClass(news.trustScore)"
                    :style="{ width: news.trustScore + '%' }"
                  ></div>
                </div>
              </div>
              <div class="featured-content">
                <div class="trust-badge" :class="getTrustLevelClass(news.trustScore)">
                  <span class="trust-label">Credibility</span>
                  <span class="trust-score">{{ news.trustScore }}%</span>
                </div>
                <h3 class="featured-title">{{ news.title }}</h3>
                <p class="featured-summary">{{ news.summary }}</p>
                <div class="featured-meta">
                  <span class="featured-source">{{ news.source }}</span>
                  <span class="featured-date">{{ formatDate(news.date) }}</span>
                </div>
              </div>
            </router-link>
          </div>
        </div>
      </section>
      
      <!-- News List -->
      <section class="news-section">
        <div class="section-header">
          <h2 class="section-title">Latest News</h2>
          <router-link to="/news" class="section-action">
            View All
            <span>→</span>
          </router-link>
        </div>
        <div v-if="newsList.length === 0" class="empty-state">
          <div class="empty-icon">📰</div>
          <p>No news content available</p>
        </div>
        <div v-else class="news-grid">
          <NewsCard 
            v-for="news in newsList" 
            :key="news.id"
            :id="news.id"
            :title="news.title"
            :summary="news.summary"
            :imageUrl="news.imageUrl || 'https://picsum.photos/id/'+(news.id%100)+'/400/225'"
            :trustScore="news.trustScore"
            :source="news.source"
            :date="news.date"
          />
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
// Import real store
import { useNewsStore } from '../store/newsStore';
import { useAuthStore } from '../stores/authStore';
import { storeToRefs } from 'pinia';
import NewsCard from '../components/NewsCard.vue';
import CategoryTabs from '../components/CategoryTabs.vue';

export default {
  name: 'Home',
  components: {
    NewsCard,
    CategoryTabs
  },
  setup() {
    const router = useRouter();
    const newsStore = useNewsStore();
    const authStore = useAuthStore();
    const { getUser: currentUser } = storeToRefs(authStore);
    
    // Get data from store
    const isLoading = computed(() => newsStore.isLoading);
    const error = computed(() => newsStore.error);
    const newsCounts = computed(() => newsStore.newsCounts);
    
    // Mock news data for demonstration
    const mockNews = [
      {
        id: 1,
        title: 'Global Climate Change Summit Reaches New Agreement',
        summary: 'Participating countries commit to carbon neutrality by 2050 to jointly address the global climate crisis.',
        trustScore: 92,
        imageUrl: 'https://picsum.photos/id/1/400/225',
        date: new Date().toISOString(),
        source: 'Environmental News',
        category: 'Environment'
      },
      {
        id: 2,
        title: 'Latest Research Shows Regular Exercise Can Extend Lifespan',
        summary: 'Scientists found that 150 minutes of moderate-intensity exercise per week can significantly reduce mortality rates.',
        trustScore: 85,
        imageUrl: 'https://picsum.photos/id/20/400/225',
        date: new Date().toISOString(),
        source: 'Health Science Journal',
        category: 'Health'
      },
      {
        id: 3,
        title: 'New Electric Vehicle Charging Technology Released',
        summary: 'Charging time reduced by 80%, addressing electric vehicle range anxiety.',
        trustScore: 78,
        imageUrl: 'https://picsum.photos/id/3/400/225',
        date: new Date().toISOString(),
        source: 'Tech Innovation Weekly',
        category: 'Technology'
      }
    ];
    
    // Computed property: Get all news (using mock data for demonstration)
    const newsList = computed(() => {
      // If store has data, use it; otherwise use mock data for demonstration
      return newsStore.newsList.length > 0 ? newsStore.newsList : mockNews;
    });
    
    // Computed property: Get top 3 news with highest credibility as featured
    const featuredNews = computed(() => {
      const newsData = newsStore.newsList.length > 0 ? newsStore.newsList : mockNews;
      return [...newsData]
        .sort((a, b) => b.trustScore - a.trustScore)
        .slice(0, 3);
    });
    
    // Get news data
    const fetchNews = async () => {
      await newsStore.fetchNews();
    };
    
    // Get CSS class based on trust score
    const getTrustLevelClass = (trustScore) => {
      if (trustScore >= 80) return 'trust-high';
      if (trustScore >= 60) return 'trust-medium';
      return 'trust-low';
    };
    
    // Get text based on trust score
    const getTrustLevelText = (trustScore) => {
      if (trustScore >= 80) return 'High Credibility';
      if (trustScore >= 60) return 'Medium Credibility';
      return 'Low Credibility';
    };
    
    // Get icon based on trust score
    const getTrustLevelIcon = (trustScore) => {
      if (trustScore >= 80) return '✓';
      if (trustScore >= 60) return '!';
      return '×';
    };
    
    // Format date
    const formatDate = (dateString) => {
      try {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'long',
          day: 'numeric'
        });
      } catch {
        return dateString;
      }
    };
    
    // Get user role label
    const getUserRoleLabel = (role) => {
      const roleMap = {
        'ADMIN': 'Admin',
        'MEMBER': 'Member',
        'USER': 'User'
      };
      return roleMap[role] || 'Guest';
    };
    
    // Get news data when page loads
    onMounted(() => {
      fetchNews();
    });
    
    return {
      isLoading,
      error,
      newsList,
      featuredNews,
      fetchNews,
      getTrustLevelClass,
      getTrustLevelText,
      getTrustLevelIcon,
      formatDate,
      currentUser,
      getUserRoleLabel,
      newsCounts
    };
  }
};
</script>

<style scoped>
.home-page {
  width: 100%;
}

/* Welcome Section */
.welcome-section {
  background-color: var(--secondary-color);
  color: var(--primary-color);
  padding: var(--spacing-lg) 0;
  margin-bottom: var(--spacing-xl);
}

.welcome-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--spacing-md);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.welcome-title {
  font-size: var(--font-xl);
  font-weight: var(--font-bold);
  margin: 0 0 var(--spacing-xs) 0;
}

.welcome-text {
  font-size: var(--font-base);
  margin: 0;
  opacity: 0.9;
}

/* Core Features Section */
.features-section {
  margin-bottom: var(--spacing-2xl);
}

/* Loading State */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-2xl) var(--spacing-md);
  text-align: center;
  background-color: var(--card-background);
  border-radius: var(--radius-lg);
  margin-bottom: var(--spacing-xl);
  box-shadow: var(--shadow-sm);
}

.loading-spinner {
  width: 48px;
  height: 48px;
  border: 4px solid var(--divider-color);
  border-top: 4px solid var(--secondary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: var(--spacing-lg);
}

.loading-container p {
  color: var(--text-secondary);
  font-size: var(--font-base);
  margin: 0;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Error State */
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-2xl) var(--spacing-md);
  text-align: center;
  background-color: rgba(231, 76, 60, 0.05);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  margin-bottom: var(--spacing-xl);
}

.error-icon {
  font-size: 4rem;
  margin-bottom: var(--spacing-lg);
}

.error-container h3 {
  font-size: var(--font-lg);
  font-weight: var(--font-semibold);
  color: var(--error-color);
  margin: 0 0 var(--spacing-sm) 0;
}

.error-container p {
  color: var(--text-secondary);
  margin: 0 0 var(--spacing-lg) 0;
  max-width: 400px;
  line-height: var(--leading-relaxed);
}

/* Featured News Section */
.featured-section {
  margin-bottom: var(--spacing-2xl);
}

.featured-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: var(--spacing-xl);
  margin-top: var(--spacing-xl);
}

.featured-card {
  background-color: var(--card-background);
  border-radius: var(--radius-lg);
  overflow: hidden;
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-base);
  position: relative;
}

.featured-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-md);
  border-color: var(--secondary-color);
}

.featured-link {
  display: block;
  text-decoration: none;
  color: inherit;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.featured-image-wrapper {
  position: relative;
  height: 220px;
  overflow: hidden;
}

.featured-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-slow);
}

.featured-card:hover .featured-image {
  transform: scale(1.05);
}

/* Trust Indicator */
.trust-indicator {
  position: absolute;
  top: var(--spacing-md);
  right: var(--spacing-md);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-md);
  font-size: var(--font-sm);
  font-weight: var(--font-bold);
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0.9);
  z-index: 10;
  transition: all var(--transition-fast);
}

.trust-indicator.trust-high {
  color: var(--trust-high-color);
}

.trust-indicator.trust-medium {
  color: var(--trust-medium-color);
}

.trust-indicator.trust-low {
  color: var(--trust-low-color);
}

.trust-icon {
  font-size: var(--font-lg);
  font-weight: var(--font-bold);
}

/* Trust Progress Bar */
.trust-progress-bar-container {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 4px;
  background-color: rgba(0, 0, 0, 0.1);
}

.trust-progress-bar {
  height: 100%;
  transition: width var(--transition-base);
}

.trust-progress-bar.trust-high {
  background-color: var(--trust-high-color);
}

.trust-progress-bar.trust-medium {
  background-color: var(--trust-medium-color);
}

.trust-progress-bar.trust-low {
  background-color: var(--trust-low-color);
}

.featured-content {
  padding: var(--spacing-lg);
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* Trust Badge */
.trust-badge {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: 4px 10px;
  border-radius: 16px;
  font-size: var(--font-xs);
  font-weight: var(--font-semibold);
  margin-bottom: var(--spacing-sm);
}

.trust-badge.trust-high {
  background-color: var(--trust-high-bg);
  color: var(--trust-high-color);
}

.trust-badge.trust-medium {
  background-color: var(--trust-medium-bg);
  color: var(--trust-medium-color);
}

.trust-badge.trust-low {
  background-color: var(--trust-low-bg);
  color: var(--trust-low-color);
}

.featured-title {
  font-size: var(--font-xl);
  font-weight: var(--font-semibold);
  line-height: var(--leading-tight);
  margin: 0 0 var(--spacing-md) 0;
  color: var(--text-primary);
  transition: color var(--transition-fast);
}

.featured-card:hover .featured-title {
  color: var(--secondary-color);
}

.featured-summary {
  font-size: var(--font-base);
  line-height: var(--leading-relaxed);
  color: var(--text-secondary);
  margin: 0 0 var(--spacing-lg) 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex: 1;
}

.featured-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: var(--font-sm);
  color: var(--text-tertiary);
  margin-top: auto;
}

.featured-source {
  font-weight: var(--font-medium);
  color: var(--text-secondary);
}

.featured-date {
  font-style: italic;
}

/* News List Section */
.news-section {
  margin-bottom: var(--spacing-2xl);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-3xl) var(--spacing-md);
  text-align: center;
  background-color: var(--surface-background);
  border-radius: var(--radius-lg);
  border: 2px dashed var(--divider-color);
  margin-top: var(--spacing-xl);
}

.empty-icon {
  font-size: 5rem;
  margin-bottom: var(--spacing-lg);
  opacity: 0.7;
}

.empty-state p {
  font-size: var(--font-base);
  color: var(--text-secondary);
  margin: 0;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: var(--spacing-lg);
  margin-top: var(--spacing-xl);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .featured-grid {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: var(--spacing-lg);
  }
  
  .news-grid {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: var(--spacing-md);
  }
  
  .featured-image-wrapper {
    height: 180px;
  }
  
  .featured-content {
    padding: var(--spacing-md);
  }
  
  .featured-title {
    font-size: var(--font-lg);
  }
  
  .featured-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-xs);
  }
}

@media (max-width: 480px) {
  .featured-grid,
  .news-grid {
    grid-template-columns: 1fr;
    gap: var(--spacing-md);
  }
  
  .featured-image-wrapper {
    height: 160px;
  }
  
  .featured-content {
    padding: var(--spacing-sm) var(--spacing-md);
  }
  
  .trust-indicator {
    padding: var(--spacing-xs) var(--spacing-sm);
    font-size: var(--font-xs);
    top: var(--spacing-sm);
    right: var(--spacing-sm);
  }
  
  .empty-state {
    padding: var(--spacing-2xl) var(--spacing-md);
  }
  
  .empty-icon {
    font-size: 4rem;
  }
}

/* Scroll to Top Button */
.scroll-top-button {
  position: fixed;
  bottom: var(--spacing-xl);
  right: var(--spacing-xl);
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: var(--secondary-color);
  color: var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border: none;
  box-shadow: var(--shadow-md);
  transition: all var(--transition-base);
  z-index: 100;
  opacity: 0;
  visibility: hidden;
  font-size: var(--font-lg);
}

.scroll-top-button.visible {
  opacity: 1;
  visibility: visible;
}

.scroll-top-button:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
  background-color: #ffc107;
}

@media (max-width: 768px) {
  .scroll-top-button {
    bottom: var(--spacing-lg);
    right: var(--spacing-lg);
    width: 44px;
    height: 44px;
  }
}
</style>