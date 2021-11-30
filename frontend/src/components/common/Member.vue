<template>
  <router-link
    :to="{ name: 'Profile', params: { email: user.email } }"
    class="flex gap-1 items-center p-1 hover:bg-yellow-100 rounded"
  >
    <div class="w-6 h-6 rounded-full border bg-pink-100">
      <img
        v-if="user.coverPicUri"
        :src="user.coverPicUri"
        alt=""
        class="w-full h-full object-cover"
      />
    </div>
    <span class="text-sm font-medium">{{ user.name }}</span>
  </router-link>
</template>

<script>
export default {
  name: "Member",
  props: {
    user: Object,
  },
}
</script>

<style lang="scss" scoped></style>
