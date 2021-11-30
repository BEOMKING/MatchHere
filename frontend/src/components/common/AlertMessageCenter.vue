<template>
  <div class="message-container">
    <transition-group tag="ul" class="messages" name="message">
      <AlertMessage
        v-for="(message, key) in messages"
        :key="key"
        :message="message.text"
        :type="message.type"
      />
    </transition-group>
  </div>
</template>

<script>
import AlertMessage from "@/components/common/AlertMessage.vue"
import { useStore } from "vuex"
import { computed } from "vue"
export default {
  name: "AlertMessageCenter",
  components: { AlertMessage },
  setup() {
    const store = useStore()
    const messages = computed(() => store.state.alertMessages)
    return {
      messages,
    }
  },
}
</script>

<style lang="scss" scoped>
.message-container {
  @apply fixed z-50 top-20 right-6;

  .messages {
    @apply grid;
  }
}

.message-enter-active,
.message-leave-active {
  transition: all 1s ease;
}

.message-enter-from {
  @apply opacity-0;
}

.message-leave-to {
  transform: translateX(100px);
  @apply opacity-0;
}

.message-leave-active {
  @apply absolute;
}
</style>
