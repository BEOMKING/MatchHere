<template>
  <header class="page-header">
    <h2>비밀번호 찾기</h2>
    <div v-if="showAuthCodeField">
      <p>인증메일이 발송되었습니다.</p>
      <p>이메일을 확인하고 인증코드를 입력하세요.</p>
    </div>
  </header>
  <section class="form-section">
    <div class="form">
      <div class="fields">
        <EmailFormField
          :field="emailField"
          :forSignup="false"
          v-model="emailField.value"
          @onShow:authCodeField="showAuthCodeField = true"
        />
        <AuthCodeFormField
          :field="authCodeField"
          :forSignup="false"
          v-model="authCodeField.value"
          v-if="showAuthCodeField"
          @onHide:AuthCodeField="handleHideAuthCodeField"
        />
      </div>
      <SubmitButton @click="handleSubmit" :disabled="!canSubmit">
        확인
      </SubmitButton>
    </div>
    <router-link :to="{ name: 'Login' }">로그인</router-link>
  </section>
</template>

<script>
import { ref } from "vue"
import AuthCodeFormField from "@/components/common/formField/AuthCodeFormField.vue"
import EmailFormField from "@/components/common/formField/EmailFormField.vue"
import SubmitButton from "@/components/common/SubmitButton.vue"
import { AuthCodeFormFieldMaker, EmailFormFieldMaker } from "@/libs/func"

export default {
  name: "FindPWStepOne",
  components: { AuthCodeFormField, SubmitButton, EmailFormField },
  emits: ["update:step"],
  setup(_, { emit }) {
    const step = ref("sendEmail")
    const emailField = ref(new EmailFormFieldMaker())
    const showAuthCodeField = ref(false)
    const authCodeField = ref(new AuthCodeFormFieldMaker())
    const canSubmit = ref(false)

    const handleHideAuthCodeField = () => {
      showAuthCodeField.value = false
      emailField.value.disabled = true
      emailField.value.buttonLabel = "인증 완료"
      emailField.value.buttonDisabled = true
      canSubmit.value = true
    }

    const handleSubmit = () => {
      emit("update:step")
    }

    return {
      step,
      emailField,
      showAuthCodeField,
      authCodeField,
      canSubmit,
      handleSubmit,
      handleHideAuthCodeField,
    }
  },
}
</script>

<style lang="scss" scoped>
.form-section {
  @apply grid gap-2 mb-10;

  .form {
    @apply grid gap-6 w-full;

    .fields {
      @apply grid gap-4;
    }
  }
  a {
    @apply text-sm font-medium place-self-center;
  }
}

.error-section {
  @apply grid gap-2;

  h5 {
    @apply font-bold;
  }
  div {
    @apply grid gap-1 place-items-start;

    p {
      @apply text-gray-600;
    }

    button {
      @apply text-blue-500 font-bold;
    }
  }
}
</style>
