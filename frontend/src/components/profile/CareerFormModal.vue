<template>
  <Modal @closeModal="handleCloseModal">
    <h4 class="mb-4">{{ type === "CREATE" ? "경력 추가" : "경력 수정" }}</h4>
    <div class="grid gap-4 mb-10">
      <InputFormField :field="companyField" v-model="companyField.value" />
      <InputFormField :field="roleField" v-model="roleField.value" />
      <div class="field grid gap-2">
        <label class="text-sm font-medium">{{ startDate.label }}</label>
        <input
          class="py-2 px-4 border border-gray-400 rounded"
          type="date"
          name="startDate"
          :disabled="startDate.disabled"
          v-model="startDate.value"
        />
      </div>
      <div class="field grid gap-2">
        <label class="text-sm font-medium">{{ endDate.label }}</label>
        <input
          class="py-2 px-4 border border-gray-400 rounded"
          type="date"
          name="endDate"
          :disabled="endDate.disabled || isIncumbent"
          v-model="endDate.value"
        />
        <div class="flex gap-2 items-center">
          <input
            type="checkbox"
            name="is_incumbent"
            id="is_incumbent"
            v-model="isIncumbent"
          />
          <label for="is_incumbent">재직중</label>
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
import { reactive, ref } from "@vue/reactivity"
import { InputFormFieldMaker } from "@/libs/func"
import { useStore } from "vuex"
import { onMounted } from "@vue/runtime-core"

export default {
  name: "CareerFormModal",
  components: { Modal, InputFormField },
  emits: ["closeModal"],
  props: {
    careerId: Number,
    type: String,
  },
  setup(props, { emit }) {
    const store = useStore()
    const companyField = reactive(
      new InputFormFieldMaker(
        "company",
        "",
        false,
        "기업명",
        "string",
        "ex) MatchHere"
      )
    )

    const roleField = reactive(
      new InputFormFieldMaker(
        "role",
        "",
        false,
        "역할",
        "string",
        "ex) PM, 기획, 디자인 등"
      )
    )

    const startDate = reactive({
      value: "",
      label: "입사일",
      disabled: false,
    })

    const endDate = reactive({
      value: "",
      label: "퇴직일",
      disabled: false,
    })

    const isIncumbent = ref(false)

    const description = ref("")

    const checkAllValueFilled = () => {
      if (companyField.value && roleField.value && startDate.value) {
        if (endDate.value || isIncumbent.value) {
          return true
        }
      }
      store.commit("ADD_MESSAGE", {
        text: "내용을 모두 채워주세요",
        type: "warning",
      })
      return false
    }

    const checkStartEndDate = () => {
      if (!isIncumbent.value && startDate.value > endDate.value) {
        store.commit("ADD_MESSAGE", {
          text: "입사일이 퇴사일보다 늦을 수는 없어요",
          type: "warning",
        })
        return false
      }
      return true
    }

    const handleCloseModal = (payload = {}) => {
      emit("closeModal", { ...payload, type: "career" })
    }

    onMounted(async () => {
      switch (props.type) {
        case "EDIT": {
          const careerData = await store.dispatch(
            "member/getCareer",
            props.careerId
          )
          companyField.value = careerData.company
          roleField.value = careerData.role
          startDate.value = careerData.start_date
          endDate.value = careerData.end_date || ""
          isIncumbent.value = careerData.is_incumbent
          description.value = careerData.description
        }
      }
    })

    const handleSubmit = async () => {
      if (checkAllValueFilled() && checkStartEndDate()) {
        const submitData = {
          company: companyField.value,
          role: roleField.value,
          start_date: startDate.value,
          end_date: endDate.value,
          is_incumbent: isIncumbent.value,
          description: description.value,
        }
        switch (props.type) {
          case "EDIT": {
            const careerData = await store.dispatch("member/updateCareer", {
              submitData,
              careerId: props.careerId,
            })
            if (careerData) {
              handleCloseModal({ data: careerData, action: "update" })
            }
            break
          }
          case "CREATE": {
            const careerData = await store.dispatch(
              "member/createCareer",
              submitData
            )
            if (careerData) {
              handleCloseModal({ data: careerData, action: "create" })
            }
            break
          }
        }
      }
    }

    return {
      companyField,
      roleField,
      startDate,
      endDate,
      isIncumbent,
      description,
      handleSubmit,
      handleCloseModal,
    }
  },
}
</script>

<style lang="scss" scoped></style>
