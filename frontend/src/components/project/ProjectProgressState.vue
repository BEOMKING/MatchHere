<template>
  <span class="py-1 px-2 rounded font-bold text-sm" :class="colorClass">
    {{ state }}
  </span>
</template>

<script>
import { onMounted, ref } from "@vue/runtime-core"
export default {
  name: "ProjectProgressState",
  props: ["state"],
  setup(props) {
    const colorClass = ref()
    onMounted(() => {
      const state = props.state
      if (state === "프로젝트 준비 중") {
        colorClass.value = "bg-green-100 text-green-600"
      } else if (state === "프로젝트 진행 중 ") {
        colorClass.value = "bg-blue-100 text-blue-600"
      } else {
        colorClass.value = "bg-red-100 text-red-600"
      }
    })

    return { colorClass }
  },
}
</script>
