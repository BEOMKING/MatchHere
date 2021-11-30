<template>
  <div>
    <div v-if="!loading" class="grid gap-6">
      <div class="grid gap-6">
        <div class="grid gap-2">
          <SelectFormField
            :field="positionField"
            v-model="positionField.value"
            @update:modelValue="handleChangePosition"
          />
          <div class="flex gap-2 flex-wrap">
            <DetailPositionButton
              v-for="dp in displayedDpositionList"
              :key="dp.id"
              :dp="dp"
              @onUpdateStatus="handleClickDP"
            />
          </div>
        </div>
        <div class="grid gap-2">
          <TeckStackField @SelectTeckStack="handleSelectTeckStack" />
          <div class="grid gap-2">
            <ul v-for="(userLevel, key) in techList" :key="key">
              <TeckStackListItem
                :name="key"
                :userLevel="userLevel"
                @change:userLevel="handleChangeUserLevel"
                @remove:teckStack="handleRemoveTeckStack"
              />
            </ul>
          </div>
        </div>
      </div>
      <SubmitButton :disabled="!formIsValid" @click="handleSubmit">
        저장
      </SubmitButton>
    </div>
  </div>
</template>

<script>
import { computed, onMounted, reactive, ref, watch } from "vue"
import { useStore } from "vuex"
import SelectFormField from "@/components/common/formField/SelectFormField.vue"
import DetailPositionButton from "@/components/common/formField/DetailPositionButton.vue"
import TeckStackField from "@/components/common/formField/TeckStackField.vue"
import TeckStackListItem from "@/components/common/formField/TeckStackListItem.vue"
import SubmitButton from "@/components/common/SubmitButton.vue"
import { detailPositionList } from "@/libs/data"

export default {
  name: "PositionTechStack",
  components: {
    SelectFormField,
    DetailPositionButton,
    TeckStackField,
    TeckStackListItem,
    SubmitButton,
  },
  setup() {
    const store = useStore()
    const loading = ref(true)
    const positionField = reactive({
      label: "포지션",
      value: "기획자",
      placeholder: "포지션을 선택하세요",
      options: ["개발자", "기획자", "디자이너"],
    })
    const dpositionList = reactive({ ...detailPositionList })
    const selectedDpositionList = ref([])
    const techList = ref({})
    const displayedDpositionList = computed(() => {
      return dpositionList[positionField.value]
    })

    const formIsValid = computed(() => {
      return positionField.value
    })

    watch(
      () => positionField.value,
      (_, prevPosition) => {
        dpositionList[prevPosition].forEach((dp) => {
          dp.selected = false
        })
      }
    )

    const handleChangePosition = () => {
      selectedDpositionList.value = []
    }

    const handleClickDP = (id) => {
      dpositionList[positionField.value].forEach((dp) => {
        if (dp.id === id) {
          dp.selected = !dp.selected
        }
      })
    }

    const handleSelectTeckStack = (key) => {
      if (Object.keys(techList.value).includes(key)) return
      techList.value[key] = "하"
    }

    const handleChangeUserLevel = ({ key, level }) => {
      techList.value[key] = level
    }

    const handleRemoveTeckStack = (key) => {
      delete techList.value[key]
    }

    const handleSubmit = async () => {
      const data = {
        position: positionField.value,
        dpositionList: dpositionList[positionField.value]
          .filter((dp) => dp.selected)
          .map((dp) => dp.value),
        techList: { ...techList.value },
      }
      await store.dispatch("member/updateSkills", data)
    }

    onMounted(async () => {
      const skills = await store.dispatch("member/getSkills")
      positionField.value = skills.position
      dpositionList[skills.position].forEach((dp) => {
        if (skills.dpositionList.includes(dp.value)) {
          dp.selected = true
        }
      })
      techList.value = skills.techList
      loading.value = false
    })

    return {
      loading,
      positionField,
      techList,
      displayedDpositionList,
      formIsValid,
      handleClickDP,
      handleChangePosition,
      handleSelectTeckStack,
      handleChangeUserLevel,
      handleRemoveTeckStack,
      handleSubmit,
    }
  },
}
</script>

<style lang="scss" scoped></style>
