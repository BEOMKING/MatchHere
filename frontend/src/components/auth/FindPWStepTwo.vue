<template>
  <header class="page-header">
    <h2>비밀번호 찾기</h2>
    <div>
      <p>비밀번호를 초기화합니다.</p>
      <p>사용하실 새 비밀번호를 입력하세요.</p>
    </div>
  </header>
  <section class="form-section">
    <div class="form">
      <div class="fields">
        <InputFormField
          v-for="field in formFields"
          :key="field.key"
          :field="field"
          v-model="field.value"
        />
      </div>
      <SubmitButton :disabled="!canSubmit" @click="handleSubmit">
        비밀번호 변경
      </SubmitButton>
    </div>
  </section>
</template>

<script>
import InputFormField from "@/components/common/formField/InputFormField.vue"
import { computed, ref } from "vue"
import SubmitButton from "@/components/common/SubmitButton.vue"
import { InputFormFieldMaker } from "@/libs/func"
import { useStore } from "vuex"
import { useRouter } from "vue-router"

export default {
  name: "FindPWStepTwo",
  components: { InputFormField, SubmitButton },
  setup() {
    const store = useStore()
    const router = useRouter()
    const formFields = ref({
      password: new InputFormFieldMaker("password"),
      confirmPassword: new InputFormFieldMaker("confirmPassword"),
    })

    const canSubmit = computed(() =>
      Object.values(formFields.value).every(
        (f) => f.value && !Object.keys(f.errors).length
      )
    )

    const handleUpdateErrors = (validateRes) => {
      const { fieldKey, status, type } = validateRes
      if (status) {
        delete formFields.value[fieldKey].errors[type]
      } else {
        const { message } = validateRes
        formFields.value[fieldKey].errors[type] = message
      }
    }

    const checkPasswordMatch = () => {
      if (
        formFields.value.password.value !==
        formFields.value.confirmPassword.value
      ) {
        store.commit("ADD_MESSAGE", {
          text: "비밀번호가 일치하지 않습니다",
          type: "error",
        })
        return false
      }
      return true
    }

    const handleSubmit = async () => {
      if (!checkPasswordMatch) return
      const { value: password } = formFields.value.password
      console.log(password)
      try {
        await store.dispatch("auth/findPassword", { password })
        router.push({ name: "Login" })
      } catch (error) {
        console.log(error)
      }
    }

    return {
      formFields,
      canSubmit,
      handleUpdateErrors,
      handleSubmit,
    }
  },
}
</script>

<style lang="scss" scoped>
.form-section {
  .form {
    @apply grid gap-6;

    .fields {
      @apply grid gap-4;
    }
  }
}
</style>
