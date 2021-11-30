<template>
  <div class="form">
    <div class="fields">
      <div class="grid gap-4">
        <SelectFormField :field="positionField" v-model="positionField.value" />
        <div class="dplist">
          <DetailPositionButton
            v-for="dp in displayedDPList"
            :key="dp.id"
            :dp="dp"
            @onUpdateStatus="handleClickDP"
          />
        </div>
      </div>
      <div class="grid gap-2">
        <TeckStackField @SelectTeckStack="handleSelectTeckStack" />
        <div class="tslist">
          <ul v-for="(userLevel, key) in selectedTeckStackList" :key="key">
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
    <SubmitButton @click="handleSubmit">회원가입</SubmitButton>
  </div>
</template>

<script>
import { computed, reactive, ref, watch } from "vue"
import SubmitButton from "@/components/common/SubmitButton.vue"
import SelectFormField from "@/components/common/formField/SelectFormField.vue"
import DetailPositionButton from "@/components/common/formField/DetailPositionButton.vue"
import TeckStackField from "@/components/common/formField/TeckStackField.vue"
import TeckStackListItem from "@/components/common/formField/TeckStackListItem.vue"
import { detailPositionList } from "@/libs/data"
import { useStore } from "vuex"
import { useRouter } from "vue-router"

export default {
  name: "SignupStepTwo",
  components: {
    SubmitButton,
    SelectFormField,
    DetailPositionButton,
    TeckStackField,
    TeckStackListItem,
  },
  emits: ["update:step"],
  setup() {
    const store = useStore()
    const router = useRouter()
    const dpList = reactive({ ...detailPositionList })
    const positionField = reactive({
      label: "포지션",
      value: "개발자",
      placeholder: "포지션을 선택하세요",
      options: ["개발자", "기획자", "디자이너"],
    })
    const selectedTeckStackList = ref({})

    const detailPositionListField = computed(() =>
      dpList[positionField.value]
        .filter((dp) => dp.selected)
        .map((dp) => dp.value)
    )

    const displayedDPList = computed(() => {
      return dpList[positionField.value]
    })

    watch(
      () => positionField.value,
      (_, prevPosition) => {
        dpList[prevPosition].forEach((dp) => {
          dp.selected = false
        })
      }
    )

    const handleClickDP = (id) => {
      dpList[positionField.value].forEach((dp) => {
        if (dp.id === id) {
          dp.selected = !dp.selected
        }
      })
    }

    const handleSelectTeckStack = (teckStackKey) => {
      // TODO
      // [{python: "하"}, {javascript: "상"}] 에서
      // {python: "하", javascript: "상"} 으로 데이터 형태 변경
      if (selectedTeckStackList.value[teckStackKey]) return
      selectedTeckStackList.value[teckStackKey] = "하"
    }

    const handleChangeUserLevel = ({ key, level }) => {
      selectedTeckStackList.value[key] = level
    }

    const handleRemoveTeckStack = (key) => {
      delete selectedTeckStackList.value[key]
    }

    const handleSubmit = async () => {
      const formData = {
        position: positionField.value,
        dpositionList: [...detailPositionListField.value],
        techList: selectedTeckStackList.value,
      }
      try {
        await store.dispatch("auth/signup", formData)
        router.push({ name: "Login" })
      } catch (error) {
        // TODO: 에러 핸들링
        alert("회원가입에 실패했습니다")
      }
    }

    return {
      positionField,
      displayedDPList,
      selectedTeckStackList,
      detailPositionListField,
      handleClickDP,
      handleSelectTeckStack,
      handleChangeUserLevel,
      handleRemoveTeckStack,
      handleSubmit,
    }
  },
}
</script>

<style lang="scss" scoped>
.form {
  @apply grid gap-6;

  .fields {
    @apply grid gap-4;

    .dplist {
      @apply flex gap-2 flex-wrap;

      .dp {
        @apply py-2 px-4 border-gray-400 rounded bg-gray-200 text-gray-600 font-medium transition-colors;

        &.selected {
          @apply bg-blue-500 border-0 text-white font-bold;
        }

        &:hover {
          @apply bg-blue-200;
        }
      }
    }

    .tslist {
      @apply grid gap-2;
    }
  }
}
</style>
