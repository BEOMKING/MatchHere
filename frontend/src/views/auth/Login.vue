<template>
  <header class="page-header">
    <h2>로그인</h2>
    <p>여러분이 원하는 스터디와 프로젝트를 찾아보세요!</p>
  </header>
  <section class="login-section">
    <div class="login-container">
      <form class="form" @submit.prevent="handleSubmit">
        <div class="fields">
          <InputFormField
            v-for="(field, key) in formFields"
            :key="key"
            :field="field"
            v-model="field.value"
          />
        </div>
        <SubmitButton :disabled="!canSubmit"> 로그인 </SubmitButton>
      </form>
      <div class="links">
        <router-link :to="{ name: 'Signup' }" class="link">
          회원가입
        </router-link>
        <div class="divider"></div>
        <router-link :to="{ name: 'FindPassword' }" class="link">
          비밀번호 찾기
        </router-link>
      </div>
    </div>
  </section>
</template>

<script>
import { computed, ref } from "vue"
import InputFormField from "@/components/common/formField/InputFormField.vue"
import SubmitButton from "@/components/common/SubmitButton.vue"
import { InputFormFieldMaker } from "@/libs/func"

// import { emailValidator, passwordValidator } from "@/libs/validator2"
import { useStore } from "vuex"
import { useRouter } from "vue-router"

export default {
  name: "Login",
  components: { InputFormField, SubmitButton },
  setup() {
    const store = useStore()
    const router = useRouter()
    const formFields = ref({
      email: new InputFormFieldMaker("email"),
      password: new InputFormFieldMaker("password"),
    })

    const isAllfieldsFill = computed(() => {
      return Object.values(formFields.value).every((f) => f.value.length)
    })

    const isAllfieldsValid = computed(() => {
      return Object.values(formFields.value).every(
        (f) => Object.keys(f.errors).length === 0
      )
    })

    const canSubmit = computed(
      () => isAllfieldsFill.value && isAllfieldsValid.value
    )

    const handleSubmit = async () => {
      const formData = {
        email: formFields.value.email.value,
        password: formFields.value.password.value,
      }

      try {
        await store.dispatch("auth/login", formData)
        router.push({ name: "Home" })
      } catch (error) {
        console.log(error)
      }
    }

    return { formFields, canSubmit, handleSubmit }
  },
}
</script>

<style lang="scss" scoped>
.login-section {
  @apply grid gap-10;

  .login-container {
    @apply flex flex-col gap-2 items-center;

    .form {
      @apply w-full grid gap-6;

      .fields {
        @apply grid gap-4;
      }
    }

    .links {
      @apply flex gap-2;

      .link {
        @apply text-sm font-medium py-1 px-2;
      }
    }
  }
}
</style>
