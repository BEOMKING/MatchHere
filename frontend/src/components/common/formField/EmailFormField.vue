<template>
  <div class="form-field">
    <label :for="field.key" class="label">{{ field.label }}</label>
    <div class="input-wrapper">
      <div class="input_button">
        <input
          :id="field.key"
          :class="{ error: isError, disabled: field.disabled }"
          :type="field.type"
          :placeholder="field.placeholder"
          :value="modelValue"
          :disabled="field.disabled"
          @input="handleInput"
        />
        <button
          :class="{ disabled: field.buttonDisabled }"
          :disabled="field.buttonDisabled"
          @click="handleClick"
        >
          {{ field.buttonLabel }}
        </button>
      </div>
      <div class="errors" v-if="isError">
        <p class="error" v-for="error in field.errors" :key="error">
          {{ error }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from "vue"
import { useStore } from "vuex"
export default {
  name: "EmailFormField",
  props: {
    field: {
      type: Object,
      required: true,
    },
    modelValue: {
      type: String,
      required: true,
    },
    forSignup: {
      type: Boolean,
      default: true,
    },
  },
  emits: ["update:modelValue", "onShow:authCodeField"],
  setup(props, { emit }) {
    const store = useStore()
    const isError = computed(() => Object.keys(props.field.errors).length > 0)

    const handleInput = (event) => {
      emit("update:modelValue", event.target.value)
      if (props.field.validate()) {
        props.field.updateButtonDisabled(false)
      } else {
        props.field.updateButtonDisabled(true)
      }
    }

    const handleClick = async () => {
      props.field.updateButtonLabel("메일 전송 중")
      props.field.updateButtonDisabled(true)
      props.field.updateInputDisabled(true)

      let actionName = "auth/sendEmailForSignup"
      if (!props.forSignup) {
        actionName = "auth/sendEmailForFindPW"
      }
      try {
        await store.dispatch(actionName, props.field.value)
        props.field.updateButtonLabel("재전송")
        props.field.updateButtonDisabled(false)
        emit("onShow:authCodeField")
      } catch (error) {
        props.field.updateButtonLabel("메일 인증")
        props.field.updateButtonDisabled(false)
        props.field.updateInputDisabled(false)
      }
    }

    return { isError, handleInput, handleClick }
  },
}
</script>

<style lang="scss" scoped>
.form-field {
  @apply grid gap-2 w-full;

  .label {
    @apply text-sm font-medium place-self-start;
  }

  .input-wrapper {
    @apply grid gap-1;

    .input_button {
      @apply flex gap-2 w-full;

      input {
        @apply border border-gray-400 rounded py-2 px-4 outline-none flex-1;

        &:focus {
          @apply ring-2 ring-blue-500;
        }

        &.error {
          @apply border-red-500 bg-red-50;
        }

        &.disabled {
          @apply bg-gray-200 text-gray-400 cursor-not-allowed;
        }
      }

      button {
        @apply flex items-center justify-center w-24 rounded text-sm font-medium border border-gray-400 text-gray-900;

        &.disabled {
          @apply bg-gray-100 border-gray-400 text-gray-400 cursor-not-allowed;
        }

        &.active {
          @apply bg-blue-500 border-blue-500 text-white;
        }
      }
    }

    .errors {
      @apply grid gap-1;

      .error {
        @apply text-sm text-red-500;
      }
    }
  }
}
</style>
