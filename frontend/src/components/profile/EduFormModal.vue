<template>
  <Modal @closeModal="handleCloseModal">
    <h4 class="mb-4">학력 추가</h4>
    <div class="grid gap-4 mb-10">
      <InputFormField
        :field="institutionField"
        v-model="institutionField.value"
      />
      <SelectFormField :field="degreeField" v-model="degreeField.value" />
      <InputFormField :field="majorField" v-model="majorField.value" />
      <SelectFormField :field="stateField" v-model="stateField.value" />
      <div class="field grid gap-2">
        <label class="text-sm font-medium">입학 / 졸업년도</label>
        <div class="flex gap-4">
          <input
            class="flex-1 py-2 px-4 border border-gray-400 rounded"
            type="text"
            name="start-date"
            placeholder="입학년도 (ex, 2021)"
            v-model="startDate"
          />
          <input
            class="flex-1 py-2 px-4 border border-gray-400 rounded"
            type="text"
            name="end-date"
            placeholder="졸업년도 (ex, 2021)"
            :disabled="endDate.disabled"
            v-model="endDate.value"
          />
        </div>
      </div>
      <div class="field grid gap-2">
        <label class="text-sm font-medium">설명 (선택)</label>
        <textarea
          class="border border-gray-400 rounded outline-none p-4 resize-none"
          name="description"
          v-model="description"
        ></textarea>
      </div>
    </div>
    <div class="buttons flex gap-4 w-full justify-center">
      <button
        class="rounded py-2 px-6 text-sm font-bold bg-blue-500 text-white"
        @click="handleSubmit"
      >
        확인
      </button>
      <button
        class="
          rounded
          py-2
          px-6
          text-sm
          font-bold
          text-gray-600
          border border-gray-400
        "
        @click="handleCloseModal"
      >
        취소
      </button>
    </div>
  </Modal>
</template>

<script>
import Modal from "@/components/common/Modal.vue"
import InputFormField from "@/components/common/formField/InputFormField.vue"
import SelectFormField from "@/components/common/formField/SelectFormField.vue"
import { computed, reactive, ref } from "@vue/reactivity"
import { InputFormFieldMaker } from "@/libs/func"
import { useStore } from "vuex"
import { onMounted } from "@vue/runtime-core"

export default {
  name: "EduFormModal",
  components: { Modal, InputFormField, SelectFormField },
  emits: ["closeModal"],
  props: {
    educationId: Number,
    type: String,
  },
  setup(props, { emit }) {
    const store = useStore()

    const institutionField = reactive(
      new InputFormFieldMaker(
        "institution",
        "",
        false,
        "학교/소속",
        "string",
        "ex) 서울대학교"
      )
    )
    const degreeField = reactive({
      label: "학위",
      value: "학사",
      placeholder: "학위를 선택하세요",
      options: ["학사", "전문학사", "석사", "박사"],
    })
    const majorField = reactive(
      new InputFormFieldMaker(
        "major",
        "",
        false,
        "전공 (선택)",
        "string",
        "ex) 컴퓨터공학부"
      )
    )
    const stateField = reactive({
      label: "상태",
      value: "졸업",
      placeholder: "상태를 선택하세요",
      options: ["졸업", "재학", "휴학", "중퇴", "수료"],
    })
    const startDate = ref("")
    const endDate = reactive({
      value: "",
      disabled: computed(() =>
        ["재학", "휴학", "수료"].includes(stateField.value)
      ),
    })
    const description = ref("")

    const checkAllValueFilled = () => {
      if (
        degreeField.value &&
        institutionField.value &&
        startDate.value &&
        stateField.value
      ) {
        if (endDate.disabled || endDate.value) {
          return true
        }
      }
      store.commit("ADD_MESSAGE", {
        text: "내용을 모두 채워주세요",
        type: "warning",
      })
      return false
    }

    const checkStartendDate = () => {
      const regex = /^\d{4}$/
      if (
        !regex.test(startDate.value) ||
        (!endDate.disabled && !regex.test(endDate.value))
      ) {
        store.commit("ADD_MESSAGE", {
          text: "입학 / 졸업년도는 년도 4자리로 입력하세요",
          type: "warning",
        })
        return false
      }
      if (!endDate.disabled && +startDate.value > +endDate.value) {
        store.commit("ADD_MESSAGE", {
          text: "입학년도가 졸업년도보다 늦을 수는 없어요",
          type: "warning",
        })
        return false
      }
      return true
    }

    const handleCloseModal = (payload = {}) => {
      emit("closeModal", { ...payload, type: "education" })
    }

    onMounted(async () => {
      switch (props.type) {
        case "EDIT": {
          const eduData = await store.dispatch(
            "member/getEdu",
            props.educationId
          )
          institutionField.value = eduData.institution
          degreeField.value = eduData.degree
          majorField.value = eduData.major
          stateField.value = eduData.state
          startDate.value = eduData.start_date
          endDate.value = eduData.end_date || ""
          description.value = eduData.description
        }
      }
    })

    const handleSubmit = async () => {
      if (checkAllValueFilled() && checkStartendDate()) {
        const submitData = {
          institution: institutionField.value,
          degree: degreeField.value,
          major: majorField.value,
          state: stateField.value,
          start_date: startDate.value,
          end_date: endDate.value,
          description: description.value,
        }
        switch (props.type) {
          case "EDIT": {
            const education = await store.dispatch("member/updateEdu", {
              submitData,
              educationId: props.educationId,
            })
            if (education) {
              handleCloseModal({ data: education, action: "update" })
            }
            break
          }
          case "CREATE": {
            const education = await store.dispatch(
              "member/createEdu",
              submitData
            )
            if (education) {
              handleCloseModal({ data: education, action: "create" })
            }
            break
          }
        }
      }
    }

    return {
      institutionField,
      degreeField,
      majorField,
      stateField,
      startDate,
      endDate,
      description,
      handleSubmit,
      handleCloseModal,
    }
  },
}
</script>

<style lang="scss" scoped></style>
