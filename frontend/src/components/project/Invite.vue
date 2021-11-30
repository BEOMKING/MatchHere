<template>
  <div class="grid gap-2">
    <h3 class="font-medium text-xl">초대</h3>
    <div
      class="text-center rounded-md bg-blue-50 text-gray-900 font-medium py-4"
    >
      링크를 공유하고, 팀원을 초대하세요!
    </div>
    <button class="rounded-md bg-blue-500 text-white py-2" @click="handleClick">
      초대 링크 복사하기
    </button>
    <input type="hidden" :value="path" id="path" />
  </div>
</template>

<script>
import { ref } from "@vue/reactivity"
import { useRoute } from "vue-router"
export default {
  name: "Invite",
  setup() {
    const route = useRoute()

    const path = ref(`matchere.me/projects/article/${route.params.projectId}`)
    const handleClick = () => {
      const path = document.getElementById("path")
      path.setAttribute("type", "text")
      path.select()
      document.execCommand("copy")
      path.setAttribute("type", "hidden")
    }
    return { path, handleClick }
  },
}
</script>

<style lang="scss" scoped></style>
