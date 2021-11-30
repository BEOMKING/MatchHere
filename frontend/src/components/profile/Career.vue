<template>
  <div class="grid gap-10">
    <section>
      <header>
        <h3>경력</h3>
        <button
          class="add-button"
          @click="handleToggleModal({ type: 'career' })"
        >
          + 추가
        </button>
      </header>
      <div class="article-list">
        <CareerListItem
          v-for="career in careerList"
          :key="career.id"
          :career="career"
          @updateCareer="handleCareer"
        />
        <InfoMessageContainer v-if="!careerList.length">
          지금까지의 경력을 추가해주세요 ✅
        </InfoMessageContainer>
      </div>
    </section>
    <section>
      <header>
        <h3>자격증</h3>
        <button
          class="add-button"
          @click="handleToggleModal({ type: 'certification' })"
        >
          + 추가
        </button>
      </header>
      <div class="article-list">
        <CertificationListItem
          v-for="certification in certificationList"
          :key="certification.id"
          :certification="certification"
          @updateCertification="handleCertification"
        />
        <InfoMessageContainer v-if="!certificationList.length">
          가지고 있는 자격증을 추가해주세요 😶‍🌫️
        </InfoMessageContainer>
      </div>
    </section>
    <section>
      <header>
        <h3>학력</h3>
        <button
          class="add-button"
          @click="handleToggleModal({ type: 'education' })"
        >
          + 추가
        </button>
      </header>
      <div class="article-list">
        <EduListItem
          v-for="education in educationList"
          :key="education.id"
          :education="education"
          @updateEducation="handleEducation"
        />
        <InfoMessageContainer v-if="!educationList.length">
          학력을 추가해주세요 💼
        </InfoMessageContainer>
      </div>
    </section>
  </div>
  <CareerFormModal
    v-if="careerModalOpen"
    type="CREATE"
    @closeModal="handleToggleModal"
  />
  <CertificationFormModal
    v-if="certificationModalOpen"
    type="CREATE"
    @closeModal="handleToggleModal"
  />
  <EduFormModal
    v-if="eduModalOpen"
    type="CREATE"
    @closeModal="handleToggleModal"
  />
</template>

<script>
import { onMounted, ref } from "vue"
import { useStore } from "vuex"
import CareerFormModal from "@/components/profile/CareerFormModal.vue"
import CertificationFormModal from "@/components/profile/CertificationFormModal.vue"
import EduFormModal from "@/components/profile/EduFormModal.vue"
import CareerListItem from "@/components/profile/CareerListItem.vue"
import CertificationListItem from "@/components/profile/CertificationListItem.vue"
import EduListItem from "@/components/profile/EduListItem.vue"
import InfoMessageContainer from "@/components/common/InfoMessageContainer.vue"

export default {
  name: "Career",
  components: {
    CareerFormModal,
    CertificationFormModal,
    EduFormModal,
    CareerListItem,
    CertificationListItem,
    EduListItem,
    InfoMessageContainer,
  },
  setup() {
    const store = useStore()
    const careerList = ref([])
    const certificationList = ref([])
    const educationList = ref([])
    const careerModalOpen = ref(false)
    const certificationModalOpen = ref(false)
    const eduModalOpen = ref(false)

    const handleCareer = ({ action, data }) => {
      switch (action) {
        case "create": {
          careerList.value = [...careerList.value, data]
          break
        }
        case "update": {
          careerList.value = careerList.value.map((c) => {
            return c.id === data.id ? data : c
          })
          break
        }
        case "delete": {
          careerList.value = careerList.value.filter((c) => {
            return c.id !== data.id
          })
          break
        }
      }
    }
    const handleCertification = ({ action, data }) => {
      switch (action) {
        case "create": {
          certificationList.value = [...certificationList.value, { ...data }]
          break
        }
        case "update": {
          certificationList.value = certificationList.value.map((c) => {
            return c.id === data.id ? data : c
          })
          break
        }
        case "delete": {
          certificationList.value = certificationList.value.filter(
            (c) => c.id !== data.id
          )
          break
        }
      }
    }
    const handleEducation = ({ action, data }) => {
      switch (action) {
        case "create": {
          educationList.value = [...educationList.value, { ...data }]
          break
        }
        case "update": {
          educationList.value = educationList.value.map((e) => {
            return e.id === data.id ? { ...data } : e
          })
          break
        }
        case "delete": {
          educationList.value = educationList.value.filter(
            (e) => e.id !== data.id
          )
          break
        }
      }
    }

    const handleToggleModal = ({ type, action, data }) => {
      switch (type) {
        case "career": {
          careerModalOpen.value = !careerModalOpen.value
          store.commit("SET_MODAL_OPEN", careerModalOpen.value)
          handleCareer({ action, data })
          break
        }
        case "certification": {
          certificationModalOpen.value = !certificationModalOpen.value
          store.commit("SET_MODAL_OPEN", certificationModalOpen.value)
          handleCertification({ action, data })
          break
        }
        case "education": {
          eduModalOpen.value = !eduModalOpen.value
          store.commit("SET_MODAL_OPEN", eduModalOpen.value)
          handleEducation({ action, data })
          break
        }
      }
    }

    onMounted(async () => {
      const careerAll = await store.dispatch("member/getCareerAll")
      careerList.value = careerAll.careerList
      certificationList.value = careerAll.certificationList
      educationList.value = careerAll.educationList
    })

    return {
      careerList,
      certificationList,
      educationList,
      careerModalOpen,
      certificationModalOpen,
      eduModalOpen,
      handleCareer,
      handleCertification,
      handleEducation,
      handleToggleModal,
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

  .article-list {
    @apply grid gap-4;
  }
}
</style>
