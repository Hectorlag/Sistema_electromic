<template>
  <!-- Overlay Modal para mensajes importantes -->
  <Teleport to="body" v-if="showModal">
    <div class="message-overlay" @click="closeModal">
      <div class="enhanced-message" :class="type" @click.stop>
        <button class="enhanced-message-close" @click="closeModal">
          <i class="bi bi-x"></i>
        </button>
        <i class="enhanced-message-icon bi" :class="iconClass"></i>
        <div class="enhanced-message-title">{{ title }}</div>
        <div class="enhanced-message-text">{{ message }}</div>
        <div class="enhanced-message-actions">
          <button 
            class="enhanced-message-btn primary" 
            @click="handlePrimaryAction"
          >
            {{ primaryButtonText }}
          </button>
          <button 
            v-if="showSecondaryButton"
            class="enhanced-message-btn secondary" 
            @click="closeModal"
          >
            Cerrar
          </button>
        </div>
      </div>
    </div>
  </Teleport>

  <!-- Mensaje inline para errores simples -->
  <div 
    v-if="showInline" 
    class="inline-message pulse-effect" 
    :class="type"
  >
    <i class="inline-message-icon bi" :class="iconClass"></i>
    <span>{{ message }}</span>
  </div>
</template>

<script>
export default {
  name: 'EnhancedMessage',
  props: {
    type: {
      type: String,
      default: 'info',
      validator: value => ['success', 'error', 'info', 'warning'].includes(value)
    },
    message: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    },
    showModal: {
      type: Boolean,
      default: false
    },
    showInline: {
      type: Boolean,
      default: false
    },
    autoClose: {
      type: Boolean,
      default: true
    },
    autoCloseDelay: {
      type: Number,
      default: 5000
    },
    primaryButtonText: {
      type: String,
      default: 'Continuar'
    },
    showSecondaryButton: {
      type: Boolean,
      default: true
    }
  },
  emits: ['close', 'primary-action'],
  
  computed: {
    iconClass() {
      const icons = {
        success: 'bi-check-circle-fill',
        error: 'bi-exclamation-triangle-fill',
        info: 'bi-info-circle-fill',
        warning: 'bi-exclamation-circle-fill'
      };
      return icons[this.type] || icons.info;
    }
  },

  watch: {
    showModal(newVal) {
      if (newVal && this.autoClose && this.type === 'success') {
        this.startAutoClose();
      }
    },
    showInline(newVal) {
      if (newVal && this.autoClose) {
        this.startAutoClose();
      }
    }
  },

  mounted() {
    // Cerrar con Escape
    document.addEventListener('keydown', this.handleEscape);
  },

  beforeUnmount() {
    document.removeEventListener('keydown', this.handleEscape);
    if (this.autoCloseTimer) {
      clearTimeout(this.autoCloseTimer);
    }
  },

  methods: {
    closeModal() {
      this.$emit('close');
    },

    handlePrimaryAction() {
      this.$emit('primary-action');
      this.closeModal();
    },

    handleEscape(e) {
      if (e.key === 'Escape' && this.showModal) {
        this.closeModal();
      }
    },

    startAutoClose() {
      if (this.autoCloseTimer) {
        clearTimeout(this.autoCloseTimer);
      }
      
      this.autoCloseTimer = setTimeout(() => {
        this.closeModal();
      }, this.autoCloseDelay);
    }
  }
}
</script>

<style scoped>
/* ============================================ */
/* OVERLAY Y MENSAJES MODALES */
/* ============================================ */
.message-overlay {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  bottom: 0 !important;
  background: rgba(0, 0, 0, 0.5) !important;
  backdrop-filter: blur(3px) !important;
  z-index: 9999 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  padding: 20px !important;
  animation: overlayFadeIn 0.3s ease-out !important;
}

@keyframes overlayFadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.enhanced-message {
  background: white !important;
  border-radius: 16px !important;
  padding: 32px 40px !important;
  max-width: 500px !important;
  width: 90% !important;
  text-align: center !important;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3) !important;
  transform: scale(0.8) !important;
  animation: messagePopIn 0.4s ease-out forwards !important;
  position: relative !important;
}

@keyframes messagePopIn {
  from {
    transform: scale(0.8);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

.enhanced-message-icon {
  font-size: 64px !important;
  margin-bottom: 20px !important;
  display: block !important;
  animation: iconBounce 0.6s ease-out 0.2s !important;
}

@keyframes iconBounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-10px); }
  60% { transform: translateY(-5px); }
}

.enhanced-message-title {
  font-size: 24px !important;
  font-weight: 700 !important;
  margin-bottom: 12px !important;
  line-height: 1.2 !important;
}

.enhanced-message-text {
  font-size: 16px !important;
  line-height: 1.5 !important;
  margin-bottom: 24px !important;
  opacity: 0.8 !important;
  color: #555 !important;
}

.enhanced-message-close {
  position: absolute !important;
  top: 12px !important;
  right: 12px !important;
  background: none !important;
  border: none !important;
  font-size: 20px !important;
  cursor: pointer !important;
  color: #95a5a6 !important;
  transition: all 0.2s ease !important;
  padding: 4px !important;
  border-radius: 50% !important;
  width: 32px !important;
  height: 32px !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

.enhanced-message-close:hover {
  background: #ecf0f1 !important;
  color: #7f8c8d !important;
}

.enhanced-message-actions {
  display: flex !important;
  gap: 12px !important;
  justify-content: center !important;
  margin-top: 8px !important;
}

.enhanced-message-btn {
  padding: 12px 24px !important;
  border: none !important;
  border-radius: 8px !important;
  cursor: pointer !important;
  font-weight: 600 !important;
  font-size: 14px !important;
  transition: all 0.2s ease !important;
  min-width: 120px !important;
}

.enhanced-message-btn.primary {
  background: linear-gradient(135deg, #3498db, #2980b9) !important;
  color: white !important;
}

.enhanced-message-btn.primary:hover {
  background: linear-gradient(135deg, #2980b9, #1f5f8b) !important;
  transform: translateY(-1px) !important;
}

.enhanced-message-btn.secondary {
  background: #ecf0f1 !important;
  color: #7f8c8d !important;
}

.enhanced-message-btn.secondary:hover {
  background: #d5dbdb !important;
}

/* Variantes de color */
.enhanced-message.success {
  border-top: 6px solid #27ae60 !important;
}

.enhanced-message.success .enhanced-message-icon {
  color: #27ae60 !important;
}

.enhanced-message.success .enhanced-message-title {
  color: #27ae60 !important;
}

.enhanced-message.error {
  border-top: 6px solid #e74c3c !important;
}

.enhanced-message.error .enhanced-message-icon {
  color: #e74c3c !important;
}

.enhanced-message.error .enhanced-message-title {
  color: #e74c3c !important;
}

.enhanced-message.info {
  border-top: 6px solid #3498db !important;
}

.enhanced-message.info .enhanced-message-icon {
  color: #3498db !important;
}

.enhanced-message.info .enhanced-message-title {
  color: #3498db !important;
}

.enhanced-message.warning {
  border-top: 6px solid #f39c12 !important;
}

.enhanced-message.warning .enhanced-message-icon {
  color: #f39c12 !important;
}

.enhanced-message.warning .enhanced-message-title {
  color: #f39c12 !important;
}

/* ============================================ */
/* MENSAJES INLINE */
/* ============================================ */
.inline-message {
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  gap: 12px !important;
  margin: 20px auto !important;
  padding: 16px 24px !important;
  border-radius: 12px !important;
  font-size: 15px !important;
  font-weight: 600 !important;
  max-width: 500px !important;
  width: 90% !important;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1) !important;
  animation: slideInUp 0.4s ease-out !important;
  position: relative !important;
  border: 2px solid transparent !important;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.inline-message.success {
  background: linear-gradient(135deg, rgba(39, 174, 96, 0.15), rgba(39, 174, 96, 0.05)) !important;
  color: #27ae60 !important;
  border-color: rgba(39, 174, 96, 0.3) !important;
}

.inline-message.error {
  background: linear-gradient(135deg, rgba(231, 76, 60, 0.15), rgba(231, 76, 60, 0.05)) !important;
  color: #e74c3c !important;
  border-color: rgba(231, 76, 60, 0.3) !important;
}

.inline-message.info {
  background: linear-gradient(135deg, rgba(52, 152, 219, 0.15), rgba(52, 152, 219, 0.05)) !important;
  color: #3498db !important;
  border-color: rgba(52, 152, 219, 0.3) !important;
}

.inline-message.warning {
  background: linear-gradient(135deg, rgba(243, 156, 18, 0.15), rgba(243, 156, 18, 0.05)) !important;
  color: #f39c12 !important;
  border-color: rgba(243, 156, 18, 0.3) !important;
}

.inline-message-icon {
  font-size: 20px !important;
  flex-shrink: 0 !important;
}

.pulse-effect {
  animation: pulse 2s infinite !important;
}

@keyframes pulse {
  0% { box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1); }
  50% { box-shadow: 0 12px 35px rgba(52, 152, 219, 0.2); }
  100% { box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1); }
}

/* ============================================ */
/* RESPONSIVE */
/* ============================================ */
@media (max-width: 480px) {
  .enhanced-message {
    padding: 24px 20px !important;
    margin: 0 10px !important;
  }

  .enhanced-message-icon {
    font-size: 48px !important;
    margin-bottom: 16px !important;
  }

  .enhanced-message-title {
    font-size: 20px !important;
  }

  .enhanced-message-text {
    font-size: 14px !important;
  }

  .enhanced-message-actions {
    flex-direction: column !important;
  }

  .enhanced-message-btn {
    width: 100% !important;
  }

  .inline-message {
    margin: 15px 10px !important;
    padding: 12px 16px !important;
    font-size: 14px !important;
  }
}
</style>