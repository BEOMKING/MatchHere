<template>
  <span class="py-1 px-2 rounded font-bold text-sm" :class="colorClass">
    팀원 {{ state }}
  </span>
</template>

<script>
import { onMounted, ref } from "@vue/runtime-core"
export default {
  name: "RecruitmentState",
  props: ["state"],
  setup(props) {
    const colorClass = ref()
    onMounted(() => {
      const state = props.state
      if (state === "모집 중") {
        colorClass.value = "bg-green-100 text-green-600"
      } else {
        colorClass.value = "bg-red-100 text-red-600"
      }
    })

    return { colorClass }
  },
}
</script>
