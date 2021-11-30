<template>
  <div class="border rounded-2xl px-2 border-gray-400 flex justify-end gap-1">
    <span class="text-sm text-blue-600">{{ techStack }}</span>
    <button class="" @click="$emit('remove', idx)">
      <span class="material-icons text-sm text-gray-700 hover:text-gray-400"
        >close</span
      >
    </button>
  </div>
</template>

<script>
export default {
  name: "TechStackItem",
  props: {
    techStack: {
      type: String,
      required: true,
    },
    idx: {
      type: Number,
      required: true,
    },
  },
  emits: ["remove"],
  setup() {
    return {}
  },
}
</script>

<style lang="scss" scoped></style>
