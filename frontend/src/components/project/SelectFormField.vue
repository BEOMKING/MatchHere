<template>
  <div class="form-field">
    <label class="label">{{ field.label }}</label>
    <div class="select-wrapper">
      <select @change="handleChange">
        <option v-if="!field.value" disabled selected>
          {{ field.placeholder }}
        </option>
        <option
          v-for="item in field.options"
          :key="item"
          :selected="field.value === item"
        >
          {{ item }}
        </option>
      </select>
      <span class="material-icons icon">expand_more</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "SelectFormField",
  props: {
    field: {
      type: Object,
      required: true,
    },
    modelValue: {
      type: String,
    },
  },
  emits: ["update:modelValue"],
  setup(_, { emit }) {
    const handleChange = (e) => {
      emit("update:modelValue", e.target.value)
    }
    return {
      handleChange,
    }
  },
}
</script>

<style lang="scss" scoped>
.form-field {
  @apply grid gap-2 w-full;

  .label {
    @apply text-sm font-medium;
  }

  .select-wrapper {
    @apply relative;

    select {
      -moz-appearance: none; /* Firefox */
      -webkit-appearance: none; /* Safari and Chrome */
      appearance: none;
      @apply border border-gray-400 rounded py-2 px-4 w-full outline-none;

      &:focus {
        @apply ring-2 ring-blue-500;
      }

      &.error {
        @apply border-red-500 bg-red-50;
      }
    }

    .icon {
      transform: translateY(-50%);
      @apply absolute top-1/2 right-2;
    }
  }

  .errors {
    @apply grid gap-1;

    .error {
      @apply text-sm text-red-500;
    }
  }
}
</style>
