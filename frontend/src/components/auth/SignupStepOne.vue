<template>
  <div class="form">
    <div class="fields">
      <EmailFormField
        :field="emailField"
        v-model="emailField.value"
        @onShow:authCodeField="showAuthCodeField = true"
      />
      <AuthCodeFormField
        :field="authCodeField"
        v-model="authCodeField.value"
        v-if="showAuthCodeField"
        @onHide:AuthCodeField="handleHideAuthCodeField"
      />
      <InputFormField
        v-for="field in formFields"
        :key="field.key"
        :field="field"
        v-model="field.value"
      />
      <SelectFormField :field="cityField" v-model="cityField.value" />
    </div>
    <SubmitButton :disabled="!canSubmit" @click="handleSubmit">
      다음 단계로
    </SubmitButton>
  </div>
</template>

<script>
import { computed, onMounted, ref } from "vue"
import { cityList } from "@/libs/data"
import InputFormField from "@/components/common/formField/InputFormField.vue"
import EmailFormField from "@/components/common/formField/EmailFormField.vue"
import AuthCodeFormField from "@/components/common/formField/AuthCodeFormField.vue"
import SubmitButton from "@/components/common/SubmitButton.vue"
import SelectFormField from "@/components/common/formField/SelectFormField.vue"
import { useStore } from "vuex"
import {
  AuthCodeFormFieldMaker,
  EmailFormFieldMaker,
  InputFormFieldMaker,
} from "@/libs/func"
export default {
  name: "SignupStepOne",
  components: {
    InputFormField,
    EmailFormField,
    AuthCodeFormField,
    SubmitButton,
    SelectFormField,
  },
  emits: ["update:step"],
  setup(_, { emit }) {
    const store = useStore()
    const emailStep = ref(1)
    const emailField = ref(new EmailFormFieldMaker())
    const showAuthCodeField = ref(false)
    const authCodeField = ref(new AuthCodeFormFieldMaker())
    const formFields = ref({
      password: new InputFormFieldMaker("password"),
      confirmPassword: new InputFormFieldMaker("confirmPassword"),
      nickname: new InputFormFieldMaker("nickname"),
      name: new InputFormFieldMaker("name"),
    })
    const cityField = ref({
      label: "지역",
      value: "서울",
      placeholder: "지역을 선택하세요",
      options: cityList,
    })
    const loading = ref(false)

    const isAllfieldsFilled = computed(() => {
      return (
        Object.values(formFields.value).every((f) => f.value) &&
        cityField.value.value
      )
    })

    const isAllfieldsValid = computed(() =>
      Object.values(formFields.value).every(
        (f) => Object.keys(f.errors).length === 0
      )
    )

    const canSubmit = computed(
      () => isAllfieldsFilled.value && isAllfieldsValid.value
    )

    const handleUpdateInputFormFieldsErrors = (validateRes) => {
      const { fieldKey, status, type } = validateRes
      if (status) {
        delete formFields.value[fieldKey].errors[type]
      } else {
        formFields.value[fieldKey].errors[type] = validateRes.message
      }
    }

    const handleHideAuthCodeField = () => {
      showAuthCodeField.value = false
      emailField.value.disabled = true
      emailField.value.buttonLabel = "인증 완료"
      emailField.value.buttonDisabled = true
    }

    const checkConfirmPassword = () => {
      return (
        formFields.value.password.value ===
        formFields.value.confirmPassword.value
      )
    }

    const handleSubmit = async () => {
      if (!checkConfirmPassword()) {
        store.commit("ADD_MESSAGE", {
          text: "비밀번호가 일치하지 않습니다",
          type: "error",
        })
        return
      }
      const formData = {
        password: formFields.value.password.value,
        nickname: formFields.value.nickname.value,
        name: formFields.value.name.value,
        city: cityField.value.value,
      }
      loading.value = true
      // TODO: 에러 핸들링
      const isDuplicated = await store.dispatch("auth/submitStepOne", formData)
      if (!isDuplicated) {
        emit("update:step", 2)
      }
      loading.value = false
    }

    onMounted(() => {
      if (store.getters["auth/getStep"] === 2) {
        const { password, nickname, name, city } =
          store.getters["auth/getSIgnupFormData"]
        formFields.value.password.value = password
        formFields.value.confirmPassword.value = password
        formFields.value.nickname.value = nickname
        formFields.value.name.value = name
        cityField.value.value = city
      }
    })

    return {
      emailStep,
      emailField,
      showAuthCodeField,
      authCodeField,
      formFields,
      cityField,
      canSubmit,
      handleHideAuthCodeField,
      handleUpdateInputFormFieldsErrors,
      handleSubmit,
    }
  },
}
</script>

<style lang="scss" scoped>
.form {
  @apply grid gap-6;
  .fields {
    @apply grid gap-4;
  }
}
</style>
