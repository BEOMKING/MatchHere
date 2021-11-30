<template>
  <div
    class="flex justify-between items-center hover:bg-blue-50 cursor-pointer"
    @click="isApplicationModalActivation = true"
  >
    <div class="flex gap-2 items-center">
      <img :src="profilePic" alt="" class="w-8 h-8" />
      <p class="text-gray-900 font-medium">{{ applyer.name }}</p>
    </div>
    <div
      class="bg-blue-100 text-gray-900 font-bold text-sm px-4 py-1 rounded-lg"
    >
      {{ applyer.role }}
    </div>
  </div>
  <ApplicationModal
    @close="isApplicationModalActivation = false"
    @accept="handleAccept"
    @refuse="handleRefuse"
    v-if="isApplicationModalActivation"
    :projectId="projectId"
    :memberId="applyer.memberId"
  />
</template>

<script>
import { ref } from "vue"
import ApplicationModal from "@/components/project/ApplicationModal.vue"

export default {
  name: "ApplyerItem",
  components: { ApplicationModal },
  props: {
    applyer: Object,
    projectId: Number,
  },
  emits: ["accept", "refuse"],
  setup(props, { emit }) {
    const profilePic = ref(require("@/assets/images/test-profile.png"))

    const isApplicationModalActivation = ref(false)

    const handleAccept = () => {
      emit("accept", props.applyer)
      isApplicationModalActivation.value = false
    }
    const handleRefuse = async () => {
      emit("refuse", props.applyer.memberId)
      isApplicationModalActivation.value = false
    }
    return {
      profilePic,
      isApplicationModalActivation,
      handleAccept,
      handleRefuse,
    }
  },
}
</script>

<style lang="scss" scoped></style>
