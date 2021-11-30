<template>
  <main>
    <header class="page-header">
      <h2>프로필</h2>
      <div>
        <p>다른 사람이 나를 확인할 수 있는 프로필 열심히 가꾸자!</p>
      </div>
    </header>
    <EditProfileNav
      :activeSectionId="activeSectionId"
      @update:activeSectionId="handleUpdateSectionId"
    />
    <component :is="currentEditSection" />
  </main>
</template>

<script>
import EditProfileNav from "@/components/profile/EditProfileNav.vue"
import BasicInfo from "@/components/profile/BasicInfo.vue"
import PositionTechStack from "@/components/profile/PositionTechStack.vue"
import Career from "@/components/profile/Career.vue"
import SNSPortfolio from "@/components/profile/SNSPortfolio.vue"
import { computed, ref } from "vue"

export default {
  name: "EditProfile",
  components: {
    EditProfileNav,
    BasicInfo,
    PositionTechStack,
    Career,
    SNSPortfolio,
  },
  setup() {
    const activeSectionId = ref(1)
    const currentEditSection = computed(() => {
      switch (activeSectionId.value) {
        case 1: {
          return BasicInfo
        }
        case 2: {
          return PositionTechStack
        }
        case 3: {
          return Career
        }
        default: {
          return SNSPortfolio
        }
      }
    })

    const handleUpdateSectionId = (id) => {
      activeSectionId.value = id
    }

    return {
      activeSectionId,
      currentEditSection,
      handleUpdateSectionId,
    }
  },
}
</script>

<style></style>
