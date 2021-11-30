<template>
  <div class="form-field">
    <label :for="field.key" class="label">{{ field.label }}</label>
    <div class="input-wrapper">
      <input
        :id="field.key"
        :class="{ error: isError, disabled: field.disabled }"
        :type="field.type"
        :placeholder="field.placeholder"
        :value="modelValue"
        :disabled="field.disabled"
        @input="handleInput"
      />
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
export default {
  name: "InputFormField",
  props: {
    field: {
      type: Object,
      required: true,
    },
    formFields: {
      type: Object,
      required: true,
    },
    modelValue: {
      type: [String, Number],
      required: true,
    },
  },
  emits: ["update:modelValue", "update:errors"],
  setup(props, { emit }) {
    const isError = computed(() => Object.keys(props.field.errors).length > 0)

    const handleValidate = (value, fieldKey) => {
      const { validators } = props.field
      validators.every((validator) => {
        const validateRes = validator(value, fieldKey, props.formFields)
        emit("update:errors", validateRes)
        return validateRes.status
      })
    }

    const handleInput = (event) => {
      emit("update:modelValue", event.target.value)
      handleValidate(event.target.value, props.field.key)
    }

    return { isError, handleInput }
  },
}
</script>

<style lang="scss" scoped>
.form-field {
  @apply grid gap-2 w-full;

  .label {
    @apply text-sm font-medium;
  }

  .input-wrapper {
    @apply grid gap-1;

    input {
      @apply border border-gray-400 rounded py-2 px-4 outline-none;

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

    .errors {
      @apply grid gap-1;

      .error {
        @apply text-sm text-red-500;
      }
    }
  }
}
</style>
