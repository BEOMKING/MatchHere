<template>
  <nav v-if="!isChatDetail">
    <router-link to="/study">
      <span class="material-icons">book</span>
      <span class="label">스터디</span>
    </router-link>
    <router-link to="/projects">
      <span class="material-icons">work</span>
      <span class="label">프로젝트</span>
    </router-link>
    <router-link to="/group">
      <span class="material-icons">group</span>
      <span class="label">그룹</span>
    </router-link>
    <router-link :to="{ name: 'ChatList' }">
      <span class="material-icons">chat</span>
      <span class="label">채팅</span>
    </router-link>
    <router-link :to="{ name: 'Login' }" v-if="!isAuthenticated">
      <span class="material-icons">person</span>
      <span class="label">로그인</span>
    </router-link>
    <router-link
      :to="{ name: 'Profile', params: { email: user.email } }"
      v-else
    >
      <span class="material-icons">person</span>
      <span class="label">프로필</span>
    </router-link>
  </nav>
</template>

<script>
import { computed } from "@vue/reactivity"
import { useStore } from "vuex"
export default {
  name: "BottomNav",
  props: ["isChatDetail"],
  setup() {
    const store = useStore()
    const isAuthenticated = computed(
      () => store.getters["auth/getIsAuthenticated"]
    )
    const user = computed(() => store.state.member.user)
    return { isAuthenticated, user }
  },
}
</script>

<style lang="scss" scoped>
nav {
  @apply sticky bottom-0 left-0 w-full rounded-t-2xl border-t border-gray-200 py-2 px-6 flex md:hidden items-center justify-between bg-white;

  a {
    @apply grid w-14 justify-items-center;

    span {
      @apply text-gray-400;
    }

    .label {
      @apply text-xs;
    }

    &.router-link-exact-active span {
      @apply text-indigo-900;
    }
  }
}
</style>
