<template>
  <div class="register-container">
    <div class="register-form">
      <h2>User Registration</h2>
      <form @submit.prevent="handleSubmit">
        <!-- Avatar Upload -->
        <div class="form-group avatar-upload">
          <label>Profile Avatar</label>
          <div class="avatar-preview" :class="{ 'has-avatar': avatarPreview }" @click="triggerFileInput">
            <img v-if="avatarPreview" :src="avatarPreview" alt="Avatar Preview" class="preview-img" />
            <div v-else class="placeholder">Click to upload avatar</div>
            <input
              ref="fileInput"
              type="file"
              accept="image/*"
              @change="handleAvatarUpload"
              style="display: none"
            />
          </div>
          <p class="avatar-hint">Click the avatar area to upload, supporting JPG, PNG formats</p>
        </div>

        <div class="form-row">
          <!-- Last Name -->
          <div class="form-group half">
            <label for="lastName">Last Name</label>
            <input
              id="lastName"
              v-model="formData.lastName"
              type="text"
              placeholder="Enter your last name"
              required
            />
          </div>
          
          <!-- First Name -->
          <div class="form-group half">
            <label for="firstName">First Name</label>
            <input
              id="firstName"
              v-model="formData.firstName"
              type="text"
              placeholder="Enter your first name"
              required
            />
          </div>
        </div>

        <div class="form-group">
          <label for="username">Username</label>
          <input
            id="username"
            v-model="formData.username"
            type="text"
            placeholder="Set your username"
            required
          />
        </div>

        <div class="form-group">
          <label for="email">Email Address</label>
          <input
            id="email"
            v-model="formData.email"
            type="email"
            placeholder="Enter your email address"
            required
          />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="formData.password"
            type="password"
            placeholder="Set your password"
            required
          />
        </div>

        <div class="form-group">
          <label for="confirmPassword">Confirm Password</label>
          <input
            id="confirmPassword"
            v-model="formData.confirmPassword"
            type="password"
            placeholder="Enter password again"
            required
          />
        </div>
        
        <!-- Error Message -->
        <span v-if="error" class="error-message">{{ error }}</span>

        <button type="submit" :disabled="loading" class="register-button">
          {{ loading ? 'Registering...' : 'Register' }}
        </button>

        <div class="login-link">
          Already have an account? <router-link to="/login">Login</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'

export default {
  name: 'RegisterPage',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    
    // Form data
    const formData = ref({
      username: '',
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      confirmPassword: ''
    })
    
    // Avatar related
    const avatarPreview = ref('')
    const avatarFile = ref(null)
    const fileInput = ref(null)
    
    // Status
    const loading = ref(false)
    const error = ref('')
    
    // Handle avatar upload
    const handleAvatarUpload = (event) => {
      const file = event.target.files[0]
      if (file) {
        // Check file type
        if (!file.type.match('image/jpeg') && !file.type.match('image/png')) {
          error.value = 'Please upload JPG or PNG format images'
          return
        }
        
        // Check file size (5MB limit)
        if (file.size > 5 * 1024 * 1024) {
          error.value = 'Image size cannot exceed 5MB'
          return
        }
        
        avatarFile.value = file
        
        // 创建预览
        const reader = new FileReader()
        reader.onload = (e) => {
          avatarPreview.value = e.target.result
        }
        reader.readAsDataURL(file)
        
        error.value = ''
      }
    }
    
    // Click avatar area to trigger file selection
    const triggerFileInput = () => {
      fileInput.value.click()
    }
    
    // Form validation
    const validateForm = () => {
      // Reset error message
      error.value = ''
      
      // Validate first and last name
      if (!formData.value.firstName.trim()) {
        error.value = 'Please enter your first name'
        return false
      }
      
      if (!formData.value.lastName.trim()) {
        error.value = 'Please enter your last name'
        return false
      }
      
      // Validate username
      if (!formData.value.username.trim() || formData.value.username.length < 3) {
        error.value = 'Username must be at least 3 characters'
        return false
      }
      
      // Validate email
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(formData.value.email)) {
        error.value = 'Please enter a valid email address'
        return false
      }
      
      // Validate password
      if (formData.value.password.length < 6) {
        error.value = 'Password must be at least 6 characters'
        return false
      }
      
      // Validate password match
      if (formData.value.password !== formData.value.confirmPassword) {
        error.value = 'The passwords you entered do not match'
        return false
      }
      
      return true
    }
    
    // Handle form submission
    const handleSubmit = async () => {
      // Validate form
      if (!validateForm()) {
        return
      }
      
      loading.value = true
      
      try {
        // 构建用户数据对象
        const userData = {
          ...formData.value,
          // 在实际应用中，这里应该上传头像文件到服务器
          // 但由于我们使用的是模拟数据，这里直接传递Base64预览
          avatar: avatarPreview.value || null
        }
        
        // 调用注册方法
        const success = await authStore.register(userData)
        
        if (success) {
          // Registration successful, redirect to home page
          router.push('/')
        } else {
          // Show error from auth store
          error.value = authStore.getError || 'Registration failed, please try again later'
        }
      } catch (err) {
        error.value = 'An error occurred during registration'
        console.error('Registration error:', err)
      } finally {
        loading.value = false
      }
    }
    
    return {
      formData,
      loading,
      error,
      avatarPreview,
      fileInput,
      handleSubmit,
      handleAvatarUpload,
      triggerFileInput
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20px;
}

.register-form {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 400px;
}

.register-form h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

/* 表单行 - 用于并排的字段 */
.form-row {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.form-row .form-group.half {
  flex: 1;
  margin-bottom: 0;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #555;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #4a90e2;
}

/* 头像上传样式 */
.avatar-upload {
  text-align: center;
  margin-bottom: 25px;
}

.avatar-preview {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 2px dashed #ddd;
  margin: 0 auto 10px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-color: #f9f9f9;
  transition: border-color 0.3s, background-color 0.3s;
}

.avatar-preview:hover {
  border-color: #4a90e2;
  background-color: #f0f7ff;
}

.avatar-preview.has-avatar {
  border-style: solid;
  border-color: #ddd;
}

.preview-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.placeholder {
  color: #999;
  font-size: 14px;
  text-align: center;
  padding: 10px;
}

.avatar-hint {
  font-size: 12px;
  color: #999;
  margin: 5px 0 0;
}

/* 错误消息样式 */
.error-message {
  color: #e74c3c;
  font-size: 14px;
  margin: 10px 0 15px;
  display: block;
  text-align: center;
}

/* 按钮样式 */
.register-button {
  width: 100%;
  padding: 12px;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 10px;
}

.register-button:hover:not(:disabled) {
  background-color: #357abd;
}

.register-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

/* 登录链接样式 */
.login-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.login-link a {
  color: #4a90e2;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}

/* 响应式调整 */
@media (max-width: 480px) {
  .form-row {
    flex-direction: column;
    gap: 20px;
  }
  
  .register-form {
    padding: 30px 20px;
  }
}
</style>