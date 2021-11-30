<template>
  <div class="grid gap-10">
    <section>
      <header>
        <h3>포트폴리오</h3>
        <button class="flex">
          <span
            class="material-icons-outlined text-gray-400 hover:text-blue-500"
            @click="portfolioModalOpen = true"
          >
            settings
          </span>
        </button>
      </header>
      <div class="grid gap-4">
        <template v-if="portfolio.id || portfolioUri">
          <PortfolioFile :portfolio="portfolio" />
          <PortfolioUri :portfolioUri="portfolioUri" />
        </template>
        <InfoMessageContainer v-else>
          다른 사람들이 볼 수 있도록 포트폴리오를 추가해주세요 😎
        </InfoMessageContainer>
      </div>
    </section>
    <section>
      <header>
        <h3>SNS 링크</h3>
        <button class="flex">
          <span
            class="material-icons-outlined text-gray-400 hover:text-blue-500"
            @click="SNSModalOpen = true"
          >
            settings
          </span>
        </button>
      </header>
      <div class="grid gap-4">
        <SNSListItem v-for="sns in snsList" :key="sns.id" :sns="sns" />
        <InfoMessageContainer v-if="!snsList.length">
          Github, Facebook 등 SNS를 추가해주세요 😍
        </InfoMessageContainer>
      </div>
    </section>
    <PortfolioFormModal
      v-if="portfolioModalOpen"
      @closeModal="portfolioModalOpen = false"
      @updatePortfolio="handleUpdatePortfolio"
      :file="portfolio"
      :uri="portfolioUri"
    />
    <SNSFormModal
      v-if="SNSModalOpen"
      @closeModal="SNSModalOpen = false"
      @updateSNS="handleUpdateSNS"
      :snsList="snsList"
    />
  </div>
</template>

<script>
import { onMounted, ref } from "@vue/runtime-core"
import { useStore } from "vuex"
import PortfolioFormModal from "@/components/profile/PortfolioFormModal.vue"
import SNSFormModal from "@/components/profile/SNSFormModal.vue"
import PortfolioFile from "@/components/profile/PortfolioFile.vue"
import PortfolioUri from "@/components/profile/PortfolioUri.vue"
import SNSListItem from "@/components/profile/SNSListItem.vue"
import InfoMessageContainer from "@/components/common/InfoMessageContainer.vue"

export default {
  name: "SNSPortfolio",
  components: {
    PortfolioFormModal,
    SNSFormModal,
    PortfolioFile,
    PortfolioUri,
    SNSListItem,
    InfoMessageContainer,
  },
  setup() {
    const store = useStore()
    const portfolio = ref({
      id: "",
      file_name: "",
      file_type: "",
      download_uri: "",
    })
    const portfolioUri = ref("")
    const snsList = ref([])
    const portfolioModalOpen = ref(false)
    const SNSModalOpen = ref(false)

    const handleUpdatePortfolio = (data) => {
      if (data.portfolio) {
        portfolio.value = { ...data.portfolio }
      }
      portfolioUri.value = data.portfolio_uri || ""
    }

    const handleUpdateSNS = (data) => {
      snsList.value = [...data.snsList]
    }

    onMounted(async () => {
      const data = await store.dispatch("member/getSNSPortfolio")
      if (data.portfolio) {
        portfolio.value = { ...data.portfolio }
      }
      portfolioUri.value = data.portfolio_uri || ""
      snsList.value = [...data.snsList]
    })
    return {
      portfolio,
      portfolioUri,
      snsList,
      SNSModalOpen,
      portfolioModalOpen,
      handleUpdatePortfolio,
      handleUpdateSNS,
    }
  },
}
</script>

<style lang="scss" scoped>
section {
  @apply grid gap-4;

  header {
    @apply flex items-center justify-between;

    .add-button {
      @apply text-blue-600 font-medium py-1 px-2 rounded transition-colors hover:text-white hover:bg-blue-600;
    }
  }
}
</style>
