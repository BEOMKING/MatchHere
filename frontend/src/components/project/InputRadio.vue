<template>
  <div class="grid gap-2">
    <p class="text-sm font-medium">{{ field.label }}</p>
    <div class="grid gap-2">
      <div v-for="(option, index) in field.stateList" :key="option">
        <input
          :name="field.key"
          type="radio"
          :id="field.idList[index]"
          :value="option"
          @change="handleChange"
          :checked="option === field.value"
        />
        <label :for="field.idList[index]" class="text-gray-900 font-medium">{{
          option
        }}</label>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "InputRadio",
  props: {
    field: {
      type: Object,
    },
    modelValue: {
      type: String,
    },
  },
  emits: ["update:modelValue"],
  setup(props, { emit }) {
    const handleChange = (e) => {
      console.log(e.target.value)
      emit("update:modelValue", e.target.value)
    }

    return { handleChange }
  },
}
</script>

<style lang="scss" scoped></style>
