<template>
  <header class="page-header">
    <div class="inner">
      <h2>회원가입</h2>
      <div class="buttons">
        <button
          class="button"
          :class="{ active: currentStep === 1 }"
          @click="handleChangeStep(1)"
        >
          Step 1
        </button>
        <div class="divider"></div>
        <button
          class="button"
          :class="{ active: currentStep === 2 }"
          :disabled="maxStep === 1"
          @click="handleChangeStep(2)"
        >
          Step 2
        </button>
      </div>
    </div>
  </header>
  <section class="signup-section">
    <keep-alive>
      <component :is="currentComponent" @update:step="handleChangeStep" />
    </keep-alive>
  </section>
</template>

<script>
import SignupStepOne from "@/components/auth/SignupStepOne.vue"
import SignupStepTwo from "@/components/auth/SignupStepTwo.vue"
import { computed, ref } from "vue"
import { useStore } from "vuex"

export default {
  name: "Signup",
  components: { SignupStepOne },
  setup() {
    const store = useStore()
    const currentStep = ref(store.getters["auth/getStep"])
    const currentComponent = computed(() => {
      switch (currentStep.value) {
        case 1: {
          return SignupStepOne
        }
        default: {
          return SignupStepTwo
        }
      }
    })
    const maxStep = computed(() => store.getters["auth/getStep"])

    const handleChangeStep = (step) => {
      currentStep.value = step
    }

    return { currentComponent, currentStep, maxStep, handleChangeStep }
  },
}
</script>

<style lang="scss" scoped>
.page-header {
  .inner {
    @apply flex items-center justify-between;

    .buttons {
      @apply flex gap-2;

      button {
        @apply text-gray-300;

        &.active {
          @apply text-gray-900 font-medium;
        }
      }
    }
  }
}
</style>
