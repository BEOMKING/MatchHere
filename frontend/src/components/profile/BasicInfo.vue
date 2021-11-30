<template>
  <form @submit.prevent="handleSubmit">
    <div class="fields">
      <InputFormField :field="emailFormField" v-model="emailFormField.value" />
      <InputFormField
        :field="nicknameFormField"
        v-model="nicknameFormField.value"
      />
      <InputFormField :field="nameFormField" v-model="nameFormField.value" />
      <SelectFormField :field="cityFormField" v-model="cityFormField.value" />
      <div class="form-field">
        <label :for="bioFormField.key">{{ bioFormField.label }}</label>
        <textarea
          :id="bioFormField.key"
          :placeholder="bioFormField.placeholder"
          v-model="bioFormField.value"
        ></textarea>
      </div>
    </div>
    <SubmitButton :disabled="!formIsValid">저장</SubmitButton>
  </form>
</template>

<script>
import { InputFormFieldMaker } from "@/libs/func"
import { computed, onMounted, reactive } from "vue"
import { useStore } from "vuex"
import InputFormField from "@/components/common/formField/InputFormField.vue"
import SelectFormField from "@/components/common/formField/SelectFormField.vue"
import { cityList } from "@/libs/data"
import SubmitButton from "@/components/common/SubmitButton.vue"

export default {
  name: "BasicInfo",
  components: { InputFormField, SelectFormField, SubmitButton },
  setup() {
    const store = useStore()
    const emailFormField = reactive(
      new InputFormFieldMaker("email", "kepy1106@gmail.com", true)
    )
    const nicknameFormField = reactive(new InputFormFieldMaker("nickname"))
    const nameFormField = reactive(new InputFormFieldMaker("name"))
    const cityFormField = reactive({
      label: "지역",
      value: "서울",
      placeholder: "지역을 선택하세요",
      options: cityList,
    })
    const bioFormField = reactive({
      key: "bio",
      label: "자기소개",
      value: "",
      placeholder: "자기소개를 입력하세요",
    })

    const formIsValid = computed(() => {
      return (
        !Object.keys(nicknameFormField.errors).length &&
        !Object.keys(nameFormField.errors).length
      )
    })

    const submitData = computed(() => ({
      nickname: nicknameFormField.value,
      name: nameFormField.value,
      city: cityFormField.value,
      bio: bioFormField.value,
    }))

    const handleSubmit = async () => {
      await store.dispatch("member/updateBasicInfo", submitData.value)
    }

    onMounted(async () => {
      const { nickname, name, bio, city } = await store.dispatch(
        "member/getBasicInfo"
      )
      nicknameFormField.value = nickname
      nameFormField.value = name
      cityFormField.value = city
      bioFormField.value = bio || ""
    })

    return {
      emailFormField,
      nicknameFormField,
      nameFormField,
      cityFormField,
      bioFormField,
      formIsValid,
      handleSubmit,
    }
  },
}
</script>

<style lang="scss" scoped>
form {
  @apply grid gap-6;

  .fields {
    @apply grid gap-4;
  }
}

.form-field {
  @apply grid gap-2 w-full;
  label {
    @apply text-sm font-medium place-self-start;
  }
  textarea {
    @apply p-4 border border-gray-400 rounded resize-none outline-none;

    &:focus {
      @apply ring-2 ring-blue-500;
    }
  }
}
</style>
