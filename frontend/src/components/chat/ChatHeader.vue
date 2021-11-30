<template>
  <div class="flex justify-between py-2 px-4 items-center shadow">
    <div class="flex gap-2 items-center">
      <span
        class="material-icons text-gray-900 cursor-pointer"
        @click="handleClick"
      >
        chevron_left
      </span>
      <p class="text-gray-900">{{ targetNickname }}</p>
    </div>
    <!-- <p class="text-red-500 text-sm">채팅 목록 삭제</p> -->
  </div>
</template>

<script>
import { useRouter } from "vue-router"
export default {
  name: "ChatHeader",
  props: ["targetNickname"],
  setup() {
    const router = useRouter()
    const handleClick = () => {
      router.push({ name: "ChatList" })
      // router.go(-1)
    }
    return { handleClick }
  },
}
</script>

<style lang="scss" scoped></style>
