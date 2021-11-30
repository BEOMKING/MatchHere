<template>
  <nav v-if="!isChatDetail">
    <div class="nav-center">
      <router-link class="logo" to="/">MatchHere</router-link>
      <div class="navs">
        <router-link :to="{ name: 'StudyList' }">스터디</router-link>
        <router-link to="/">프로젝트</router-link>
        <router-link to="/chat">채팅</router-link>
      </div>
      <div class="buttons">
        <router-link :to="{ name: 'Login' }" v-if="!isAuthenticated">
          로그인
        </router-link>
        <template v-else>
          <router-link
            :to="{
              name: 'Profile',
              params: { email: user.email },
            }"
          >
            프로필
          </router-link>
          <button @click="handleClickLogoutBtn">로그아웃</button>
        </template>
      </div>
    </div>
  </nav>
</template>

<script>
import { computed } from "vue"
import { useStore } from "vuex"
import { useRouter } from "vue-router"
export default {
  name: "MainNav",
  props: ["isChatDetail"],
  setup() {
    const store = useStore()
    const router = useRouter()
    const user = computed(() => store.state.member.user)
    const isAuthenticated = computed(
      () => store.getters["auth/getIsAuthenticated"]
    )

    const handleClickLogoutBtn = () => {
      store.dispatch("auth/logout")
      router.push({ name: "Home" })
    }

    return {
      user,
      isAuthenticated,
      handleClickLogoutBtn,
    }
  },
}
</script>

<style lang="scss" scoped>
nav {
  @apply w-full border-b border-gray-200 md:shadow-md;

  .nav-center {
    @apply px-4 md:px-6 flex items-center justify-between h-16;

    .navs {
      @apply hidden md:flex gap-6;

      a {
        @apply py-2 px-4 rounded text-lg font-bold;

        &.router-link-active {
          @apply bg-blue-100 text-blue-900;
        }

        &:hover:not(.router-link-active) {
          @apply bg-blue-50;
        }
      }
    }

    .buttons {
      @apply flex gap-2;

      a,
      button {
        @apply font-medium py-2 px-4 rounded transition-colors;

        &:hover {
          @apply bg-gray-100;
        }
      }
    }
  }
}
</style>
