<template>
  <nav>
    <router-link :to="{ name: 'Profile', params: { email } }">
      프로필
    </router-link>
    <router-link to="/">프로젝트</router-link>
  </nav>
</template>

<script>
export default {
  name: "ProfileNav",
  props: {
    email: String,
  },
}
</script>

<style></style>
