<template>
  <component :is="currentComponent" @update:step="handleUpdateStep" />
</template>

<script>
import { computed, ref } from "@vue/reactivity"
import FindPWStepOne from "@/components/auth/FindPWStepOne.vue"
import FindPWStepTwo from "@/components/auth/FindPWStepTwo.vue"

export default {
  name: "FindPassword",
  components: { FindPWStepOne, FindPWStepTwo },
  setup() {
    const step = ref(1)
    const currentComponent = computed(() =>
      step.value === 1 ? FindPWStepOne : FindPWStepTwo
    )

    const handleUpdateStep = () => {
      step.value = 2
    }

    return {
      currentComponent,
      handleUpdateStep,
    }
  },
}
</script>

<style></style>
