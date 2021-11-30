<template>
  <div class="form-field">
    <label :for="field.key" class="label">{{ field.label }}</label>
    <div class="input-wrapper">
      <textarea
        class="content"
        :name="field.key"
        :id="field.key"
        cols="30"
        rows="7"
        :aria-valuemin="modelValue"
        @input="handleInput"
      ></textarea>
    </div>
  </div>
</template>

<script>
export default {
  name: "TextAreaFormField",
  emits: ["update:modelValue"],
  props: {
    field: Object,
    modelValue: {
      type: [String, Number],
      required: true,
    },
  },
  setup(props, { emit }) {
    const handleInput = (event) => {
      emit("update:modelValue", event.target.value)
    }
    return { handleInput }
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

    .content {
      @apply border border-gray-400 rounded py-2 px-4 outline-none;

      &:focus {
        @apply ring-2 ring-blue-500;
      }
    }
  }
}
</style>
