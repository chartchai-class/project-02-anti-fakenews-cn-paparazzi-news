<template>
  <div class="login-container">
    <div class="login-form">
      <h1>Universal Login Portal</h1>
      <p>Login with your account credentials below</p>

      <!-- User Role Selection -->
      <div class="role-selection">
        <label class="role-label">
          <input 
            type="radio" 
            v-model="selectedRole" 
            value="user" 
            class="role-radio"
          >
          <span class="role-text">Regular User</span>
        </label>
        <label class="role-label">
          <input 
            type="radio" 
            v-model="selectedRole" 
            value="admin" 
            class="role-radio"
          >
          <span class="role-text">Administrator</span>
        </label>
      </div>
      
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">{{ selectedRole === 'admin' ? 'Administrator Username' : 'Username / Email' }}</label>
          <input
            type="text"
            id="username"
            v-model="form.username"
            :class="{ 'is-invalid': errors.username }"
            :placeholder="selectedRole === 'admin' ? 'Enter administrator username' : 'Enter your username or email'"
            :disabled="isLoading"
            required
          />
          <div v-if="errors.username" class="error-message">{{ errors.username }}</div>
        </div>
        
        <div class="form-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            v-model="form.password"
            :class="{ 'is-invalid': errors.password }"
            :placeholder="selectedRole === 'admin' ? 'Enter administrator password' : 'Enter your password'"
            :disabled="isLoading"
            required
          />
          <div v-if="errors.password" class="error-message">{{ errors.password }}</div>
        </div>
        
        <!-- Error Message -->
        <span v-if="loginError" class="error-message">{{ loginError }}</span>
        
        <div class="form-group">
          <button type="submit" class="login-button" :disabled="isLoading">
            {{ isLoading ? 'Logging in...' : (selectedRole === 'admin' ? 'Admin Login' : 'Login') }}
          </button>
        </div>
        
        <div class="login-info">
          <div class="login-tip primary">
            <small><strong>{{ selectedRole === 'admin' ? 'Administrator login will access admin dashboard' : 'System will automatically redirect based on your role' }}</strong></small>
          </div>
          <p v-if="selectedRole === 'admin'" class="admin-note">
            Test Account: admin / admin123
          </p>
          <div class="register-link">
            Don't have an account? <router-link to="/register">Register Now</router-link>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'

export default {
  name: 'LoginPage',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    
    const isLoading = ref(false)
    const loginError = ref('')
    const selectedRole = ref('user') // Default to regular user
    
    const form = reactive({
      username: '',
      password: ''
    })
    
    const errors = reactive({
      username: '',
      password: ''
    })
    
    // Form validation
    const validateForm = () => {
      let isValid = true
      
      // Reset errors
      errors.username = ''
      errors.password = ''
      
      // Validate username
      if (!form.username.trim()) {
        errors.username = 'Please enter username'
        isValid = false
      }
      
      // Validate password
      if (!form.password) {
        errors.password = 'Please enter password'
        isValid = false
      }
      
      return isValid
    }
    
    // Handle login for both user and admin
    const handleLogin = async () => {
        // Validate form
        if (!validateForm()) {
          return
        }
        
        isLoading.value = true
        loginError.value = ''
        
        try {
          // Handle special case for administrator login
          let success = false;
          
          // If administrator role is selected and credentials match test account
          if (selectedRole.value === 'admin' && form.username === 'admin' && form.password === 'admin123') {
            // 直接调用authStore的登录方法
            success = await authStore.login(form.username, form.password);
          } else {
            // Regular login flow
            success = await authStore.login(form.username, form.password);
          }
          
          if (success) {
            // Redirect based on actual user role (not selected role for security)
            if (authStore.isAdmin) {
              router.push('/admin');
            } else {
              router.push('/home');
            }
          } else {
            // Display different error messages based on selected role
            loginError.value = selectedRole.value === 'admin' 
              ? 'Administrator login failed, please check username and password' 
              : 'Login failed, please check your credentials';
          }
        } catch (error) {
          loginError.value = 'Authentication error, please try again';
          console.error('Login error:', error);
        } finally {
          isLoading.value = false;
        }
      }
    
    return {
      form,
      errors,
      isLoading,
      loginError,
      handleLogin,
      selectedRole
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  
  .role-selection {
    display: flex;
    gap: 20px;
    margin: 20px 0;
    padding: 15px;
    background: #f8f9fa;
    border-radius: 8px;
  }
  
  .role-label {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    padding: 8px 16px;
    border-radius: 6px;
    transition: all 0.3s ease;
  }
  
  .role-label:hover {
    background: #e9ecef;
  }
  
  .role-radio {
    width: 18px;
    height: 18px;
    accent-color: #409eff;
  }
  
  .role-text {
    font-size: 16px;
    font-weight: 500;
    color: #606266;
  }
  
  .role-radio:checked + .role-text {
    color: #409eff;
    font-weight: 600;
  }
  
  .admin-note {
    margin-top: 15px;
    padding: 10px;
    background: #f0f9ff;
    border: 1px solid #91d5ff;
    border-radius: 6px;
    color: #1890ff;
    font-size: 14px;
    text-align: center;
  }
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20px;
}

.login-form {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 400px;
}

.login-form h1 {
  text-align: center;
  margin-bottom: 10px;
  color: #333;
  font-size: 28px;
}

.login-form p {
  text-align: center;
  margin-bottom: 30px;
  color: #666;
  font-size: 16px;
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

.form-group input.is-invalid {
  border-color: #e74c3c;
}

.error-message {
  color: #e74c3c;
  font-size: 14px;
  margin: 10px 0 15px;
  display: block;
  text-align: center;
}

.login-button {
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

.login-button:hover:not(:disabled) {
  background-color: #357abd;
}

.login-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.login-info {
    margin-top: 20px;
  }

  .register-link {
    text-align: center;
    margin-bottom: 10px;
    color: #666;
  }

  .register-link a {
    color: #4a90e2;
    text-decoration: none;
    font-weight: 500;
  }

  .register-link a:hover {
    text-decoration: underline;
  }

  .login-subtitle {
      text-align: center;
      margin: 10px 0 20px;
      padding: 10px;
      background-color: #f5f7fa;
      border-radius: 4px;
      border-left: 3px solid #4a90e2;
    }

    .login-subtitle small {
      font-size: 13px;
      color: #555;
    }

    .login-tip {
      text-align: center;
      margin: 10px 0;
    }

    .login-tip.small {
      font-size: 12px;
      color: #888;
      font-style: italic;
    }

    .login-tip.primary {
      color: #4a90e2;
      font-weight: 500;
      font-size: 13px;
    }

@media (max-width: 480px) {
  .login-form {
    padding: 30px 20px;
  }
}
</style>