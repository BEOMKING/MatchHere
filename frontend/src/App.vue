<template>
  <div class="min-h-screen flex flex-col" :class="{ 'modal-open': modalOpen }">
    <MainNav :isChatDetail="isChatDetail" />
    <main>
      <router-view
        @routeChatDetail="isChatDetail = true"
        @routeChatList="isChatDetail = false"
      />
    </main>
    <BottomNav :isChatDetail="isChatDetail" />
    <AlertMessageCenter />
  </div>
</template>

<script>
import MainNav from "@/components/common/MainNav.vue"
import BottomNav from "@/components/common/BottomNav.vue"
import AlertMessageCenter from "@/components/common/AlertMessageCenter.vue"
import { computed, ref } from "@vue/reactivity"
import { useStore } from "vuex"

export default {
  components: { MainNav, BottomNav, AlertMessageCenter },
  setup() {
    const store = useStore()
    const isChatDetail = ref(false)
    const modalOpen = computed(() => store.state.modalOpen)
    return {
      modalOpen,
      isChatDetail,
    }
  },
}
</script>

<style lang="scss">
* {
  @apply text-gray-900;
}

#app {
  @apply min-h-screen relative flex flex-col;
}
.modal-open {
  @apply max-h-screen overflow-hidden;
}

main {
  @apply flex-1 flex flex-col;
}

.divider {
  @apply w-px bg-gray-300;
}

.page-header {
  @apply grid gap-4 mb-10;

  h2 {
    @apply font-bold text-2xl;
  }
}
</style>
