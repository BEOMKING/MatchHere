<template>
  <div class="grid gap-4 py-4">
    <div class="flex gap-4">
      <img :src="profilePic" alt="" class="w-12 h-12 rounded-full" />
      <div class="grid gap-1">
        <span class="font-medium text-gray-900">{{ comment.name }}</span>
        <span class="text-gray-500 text-sm">{{
          comment.createDate.slice(0, 10)
        }}</span>
      </div>
    </div>
    <div>{{ comment.content }}</div>
  </div>
</template>

<script>
import { ref } from "@vue/reactivity"
export default {
  name: "Comment",
  props: ["comment"],
  setup() {
    const profilePic = ref(require("@/assets/images/test-profile.png"))
    return { profilePic }
  },
}
</script>

<style lang="scss" scoped></style>
