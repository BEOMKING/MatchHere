<template>
  <div class="container">
    <section class="project-form-section">
      <header>
        <h2>프로젝트 만들기</h2>
        <p>매치히어에서 내 프로젝트를 만들고 나의 성공시대 시작됐다!</p>
      </header>
      <div class="project-form-container">
        <div
          class="form"
          v-for="(form, name, index) in formFields"
          :key="form.id"
        >
          <h3 v-if="name === 'project'">프로젝트</h3>
          <h3 v-else-if="name === 'member'">구성원</h3>
          <div class="fields" v-for="field in form" :key="field.id">
            <InputFormField
              v-if="field.type === 'string'"
              :field="field"
              :formFields="form"
              v-model="field.value"
              @update:errors="handleUpdateErrors"
            />
            <SelectFormField
              v-else-if="field.type === 'select'"
              :field="field"
              v-model="field.value"
            />
            <div v-else-if="field.type === 'search'" class="grid gap-2">
              <TechStackField @SelectTeckStack="handleSelectTechStack" />
              <div class="grid gap-2">
                <ul v-for="(userLevel, key) in field.value" :key="key">
                  <TechStackListItem
                    :name="key"
                    :userLevel="userLevel"
                    @change:userLevel="handleChangeTechLevel"
                    @remove:teckStack="handleRemoveTechStack"
                  />
                </ul>
              </div>
            </div>
            <div v-else class="form-field">
              <p class="label">{{ field.label }}</p>
              <div v-if="field.type === 'radio'">
                <div v-for="(state, index, key) in field.stateList" :key="key">
                  <div v-if="state === '클럽 멤버에게만 공개'">
                    <input
                      class="input-radio"
                      :type="field.type"
                      :id="field.idList[index]"
                      :value="state"
                      v-model="field.value"
                      :disabled="!haveClub"
                    />
                    <label
                      :for="field.idList[index]"
                      :class="{ 'text-gray-400': !haveClub }"
                      >{{ state }}</label
                    >
                  </div>
                  <div v-else>
                    <input
                      class="input-radio"
                      :type="field.type"
                      :id="field.idList[index]"
                      :value="state"
                      v-model="field.value"
                    />
                    <label :for="field.idList[index]">{{ state }}</label>
                  </div>
                </div>
              </div>
              <div v-else-if="field.type === 'file'">
                <!-- file 타입은 양방향 바인딩 할 수 없다. method로 구현 -->
                <!-- <input
                  :type="field.type"
                  accept="image/png, image/jpeg"
                  v-model="field.value"
                /> -->
                <input
                  :type="field.type"
                  accept="image/png, image/jpeg"
                  @change="selectThumbnailImage"
                />
              </div>
              <div v-else-if="field.type === 'date'">
                <input :type="field.type" v-model="field.value" />
              </div>
              <div v-else-if="field.type === 'textarea'">
                <textarea
                  :placeholder="field.placeholder"
                  v-model="field.value"
                  class="
                    w-full
                    h-32
                    border-2
                    rounded-md
                    outline-none
                    focus:ring-2 focus:ring-blue-500
                  "
                ></textarea>
              </div>
              <div v-else-if="field.type === 'number'">
                <input
                  v-if="form.hostPosition.value === field.label"
                  :type="field.type"
                  min="1"
                  max="10"
                  v-model="field.value"
                />
                <input
                  v-else
                  :type="field.type"
                  min="0"
                  max="10"
                  v-model="field.value"
                />
              </div>
            </div>
          </div>
          <hr v-if="index < Object.keys(formFields).length - 1" />
        </div>
        <SubmitButton
          v-if="projectId"
          :disabled="!canSubmit"
          @click="handleSubmit"
        >
          수정
        </SubmitButton>
        <SubmitButton v-else :disabled="!canSubmit" @click="handleSubmit">
          생성
        </SubmitButton>
      </div>
    </section>
  </div>
</template>

<script>
import InputFormField from "@/components/common/InputFormField.vue"
import SelectFormField from "@/components/project/SelectFormField.vue"
import SubmitButton from "@/components/common/SubmitButton.vue"
import TechStackField from "@/components/common/TeckStackField.vue"
import TechStackListItem from "@/components/common/TeckStackListItem.vue"

// import TechStackItem from "@/components/project/TechStackItem.vue"
import { ref, computed, onBeforeMount, watch } from "vue"
import { requiredValidator } from "@/libs/validator"
import { cityList } from "@/libs/data"
import { useStore } from "vuex"
import { useRoute, useRouter } from "vue-router"

export default {
  name: "ProjectForm",
  components: {
    InputFormField,
    SubmitButton,
    SelectFormField,
    TechStackField,
    TechStackListItem,
    // TechStackItem,
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const route = useRoute()

    const projectId = ref()
    onBeforeMount(async () => {
      projectId.value = route.params.projectId
      if (projectId.value) {
        try {
          const projectInfo = await store.dispatch(
            "project/getInfoForUpdate",
            projectId.value
          )
          console.log(projectInfo)
          for (let form in formFields.value) {
            for (let field in formFields.value[form]) {
              const backendKey = formFields.value[form][field].backendKey
              console.log(backendKey)
              console.log(projectInfo[backendKey])
              if (backendKey === "techstacks") {
                projectInfo.techstacks.forEach(
                  (item) =>
                    (formFields.value.project.techStacks.value[item.name] =
                      item.level)
                )
              } else if (backendKey === "clubId") {
                formFields.value.project.club.clubList = projectInfo.clubs
                formFields.value.project.club.options = [
                  "선택 안함",
                  ...formFields.value.project.club.clubList.map(
                    (club) => club.name
                  ),
                ]
              } else {
                formFields.value[form][field].value = projectInfo[backendKey]
              }
            }
          }
          console.log(formFields.value)
        } catch (error) {
          store.commit("ADD_MESSAGES", {
            text: error.message,
            type: "error",
          })
          router.push({
            name: "ProjectArticle",
            params: { projectId: projectId.value },
          })
        }
      } else {
        await store.dispatch("project/getMyClubList")
        formFields.value.project.club.clubList =
          store.getters["project/getMyClubList"]
        formFields.value.project.club.options = [
          "선택 안함",
          ...formFields.value.project.club.clubList.map((club) => club.name),
        ]
      }
    })

    const formFields = ref({
      project: {
        pjtName: {
          key: "pjtName",
          backendKey: "name",
          label: "프로젝트 이름",
          type: "string",
          value: "",
          notNull: true,
          placeholder: "ex) 11월까지 진행하는 사이드 프로젝트",
          errors: {},
          validators: [requiredValidator],
        },
        pjtState: {
          key: "pjtState",
          backendKey: "projectProgressState",
          label: "프로젝트 상태",
          type: "radio",
          idList: ["will", "ing", "done"],
          stateList: ["프로젝트 준비 중", "프로젝트 진행 중", "프로젝트 종료"],
          value: "프로젝트 준비 중",
          notNull: true,
          errors: {},
          validators: [],
        },
        ThumbnailImageFile: {
          // 일단 보류! 어떻게 하는건지 보자
          key: "ThumbnailImageFile",
          backendKey: "uuid",
          label: "썸네일 이미지",
          type: "file",
          value: "",
          notNull: false,
          errors: {},
          validators: [],
        },
        techStacks: {
          key: "techStacks",
          backendKey: "techstacks",
          label: "기술스택",
          type: "search",
          value: {},
          notNull: false,
          placeholder: "ex) Vue, Spring, MySQL",
          errors: {},
          validators: [],
        },
        schedule: {
          key: "schedule",
          backendKey: "schedule",
          label: "일정",
          type: "string",
          value: "",
          notNull: false,
          placeholder: "ex) 주말 10시 - 18시 / 평일 논의",
          errors: {},
          validators: [],
        },
        dueDate: {
          key: "dueDate",
          backendKey: "period",
          label: "프로젝트 마감 예정일",
          type: "date",
          value: "",
          notNull: false,
          errors: {},
          validators: [],
        },
        city: {
          key: "city",
          backendKey: "city",
          label: "지역",
          type: "select",
          placeholder: "지역을 선택하세요",
          options: ["온라인", "무관", ...cityList],
          value: "",
          notNull: true,
          errors: {},
          validators: [],
        },
        club: {
          key: "club",
          backendKey: "clubId",
          label: "소속 클럽",
          type: "select",
          placeholder: "클럽을 선택하세요",
          options: [],
          clubList: [],
          value: null,
          notNull: false,
          errors: {},
          validators: [],
        },
        introduction: {
          key: "introduction",
          backendKey: "bio",
          label: "소개",
          type: "textarea",
          value: "",
          notNull: false,
          placeholder: "해당 프로젝트에 대해 소개해주세요",
          errors: {},
          validators: [],
        },
        openScope: {
          key: "openScope",
          backendKey: "publicScope",
          label: "공개 범위",
          type: "radio",
          idList: ["all", "club", "project"],
          stateList: ["전체 공개", "클럽 멤버에게만 공개", "비공개"],
          value: "전체 공개",
          notNull: true,
          errors: {},
          validators: [],
        },
      },
      member: {
        recruitState: {
          key: "recruitState",
          backendKey: "recruitmentState",
          label: "모집 상태",
          type: "radio",
          idList: ["rec-ing", "rec-done"],
          stateList: ["모집 중", "모집 마감"],
          value: "모집 중",
          notNull: true,
          errors: {},
          validators: [],
        },
        hostPosition: {
          key: "hostPosition",
          backendKey: "hostPosition",
          label: "팀장 포지션",
          type: "select",
          placeholder: "포지션을 선택하세요",
          options: ["개발자", "디자이너", "기획자"],
          value: "",
          notNull: true,
          errors: {},
          validators: [],
        },
        developer: {
          key: "developer",
          backendKey: "developerMaxCount",
          label: "개발자",
          type: "number",
          value: 0,
          notNull: true,
          errors: {},
          validators: [],
        },
        designer: {
          key: "designer",
          backendKey: "designerMaxCount",
          label: "디자이너",
          type: "number",
          value: 0,
          notNull: true,
          errors: {},
          validators: [],
        },
        planner: {
          key: "planner",
          backendKey: "plannerMaxCount",
          label: "기획자",
          type: "number",
          value: 0,
          notNull: true,
          errors: {},
          validators: [],
        },
      },
    })

    // 팀장 포지션 watch -> maxCount 수정
    watch(
      () => formFields.value.member.hostPosition.value,
      (newVal, oldVal) => {
        // 수정일 때 최초
        if (projectId.value && !oldVal) {
          console.log(newVal)
          console.log(oldVal)
        } else {
          console.log(newVal)
          console.log(oldVal)
          Object.values(formFields.value.member).find(
            (obj) => obj.label === newVal
          ).value += 1
          if (oldVal) {
            Object.values(formFields.value.member).find(
              (obj) => obj.label === oldVal
            ).value -= 1
          }
        }
      }
    )

    // club 선택 안하면 -> 공개 범위에서 '클럽 멤버에게만 공개' disabled
    const haveClub = ref(false)
    watch(
      () => formFields.value.project.club.value,
      (newVal) => {
        haveClub.value = newVal && newVal !== "선택 안함" ? true : false
      }
    )

    // 유효성 검사
    const isAllFieldsValid = computed(() => {
      const fields = [
        ...Object.values(formFields.value.project),
        ...Object.values(formFields.value.member),
      ]
      return fields.every((f) => Object.keys(f.errors).length === 0)
    })
    // notNull인 데이터 필수입력
    const notNullCheck = computed(() => {
      const fields = [
        ...Object.values(formFields.value.project),
        ...Object.values(formFields.value.member),
      ]
      return fields.every(
        (f) =>
          (f.notNull && f.value === 0) || (f.notNull && f.value) || !f.notNull
      )
    })
    // 모집구성원 적어도 1명 이상
    const countTotalMember = computed(() => {
      const memberFields = formFields.value.member
      return (
        memberFields.developer.value +
        memberFields.designer.value +
        memberFields.planner.value
      )
    })

    const canSubmit = computed(
      // 유효성 검사 + notNull인 데이터 필수입력 + 모집구성원 적어도 1명 이상
      () =>
        isAllFieldsValid.value &&
        notNullCheck.value &&
        countTotalMember.value > 1
    )

    // 취소 버튼 누르면 기존에 선택된 것도 사라짐.
    const selectThumbnailImage = (event) => {
      if (event.target.files.length) {
        const file = event.target.files[0]
        const formData = new FormData()
        formData.append("file", file)
        formFields.value.project.ThumbnailImageFile.value = formData
      } else {
        formFields.value.project.ThumbnailImageFile.value = ""
      }
    }

    const handleUpdateErrors = (validateRes) => {
      const { fieldKey, status, type } = validateRes
      if (status) {
        delete formFields.value.project[fieldKey].errors[type]
      } else {
        formFields.value.project[fieldKey].errors[type] = validateRes.message
      }
    }

    const handleSelectTechStack = (techStackKey) => {
      if (formFields.value.project.techStacks.value[techStackKey]) return
      formFields.value.project.techStacks.value[techStackKey] = "하"
    }
    const handleChangeTechLevel = ({ key, level }) => {
      formFields.value.project.techStacks.value[key] = level
    }
    const handleRemoveTechStack = (key) => {
      delete formFields.value.project.techStacks.value[key]
    }

    const handleSubmit = async (e) => {
      const tmpFields = {
        ...formFields.value.project,
        ...formFields.value.member,
      }
      const formData = {}
      for (let field in tmpFields) {
        if (field === "ThumbnailImageFile") continue
        else if (field === "club") {
          const clubValue = tmpFields[field].value
          if (clubValue === "선택 안함" || clubValue === null) continue
          // club name을 club id로 바꾸는 작업
          formData[tmpFields[field].backendKey] = tmpFields[
            field
          ].clubList.find((club) => club.name === clubValue).id
        } else {
          formData[tmpFields[field].backendKey] = tmpFields[field].value
        }
      }

      if (tmpFields.ThumbnailImageFile.value) {
        const { id } = await store.dispatch(
          "file/uploadFile",
          tmpFields.ThumbnailImageFile.value
        )
        formData["uuid"] = id
      }

      console.log(formData)

      try {
        if (e.target.innerText === "수정") {
          console.log(projectId.value)
          console.log(formData)
          const resData = await store.dispatch("project/updateProject", {
            formData,
            projectId: projectId.value,
          })
          alert(`임시: ${resData}`)
          router.push({
            name: "ProjectArticle",
            params: { projectId: projectId.value },
          })
        } else {
          console.log(formData)
          const projectId = await store.dispatch(
            "project/createProject",
            formData
          )
          alert(`임시: ${projectId}번 글 생성 성공`)
          router.push({ name: "ProjectArticle", params: { projectId } })
        }
      } catch (error) {
        alert(error.message)
      }
    }

    return {
      projectId,
      formFields,
      haveClub,
      selectThumbnailImage,
      handleUpdateErrors,
      canSubmit,
      handleSubmit,
      handleSelectTechStack,
      handleChangeTechLevel,
      handleRemoveTechStack,
    }
  },
}
</script>

<style lang="scss" scoped>
.project-form-section {
  @apply py-10 px-4 grid gap-4;

  header {
    @apply grid gap-4;

    h2 {
      @apply font-bold text-2xl;
    }
  }

  .project-form-container {
    @apply flex flex-col gap-6 items-center;

    .form {
      @apply w-full grid gap-4;

      h3 {
        @apply font-bold text-xl;
      }

      .fields {
        @apply grid gap-4;

        .form-field {
          @apply grid gap-2 w-full;

          .label {
            @apply text-sm font-medium;
          }
          input,
          select {
            @apply border border-gray-400 rounded py-2 px-4 outline-none w-full bg-white;

            &:focus {
              @apply ring-2 ring-blue-500;
            }

            &.error {
              @apply border-red-500 bg-red-50;
            }
          }
          .input-radio {
            @apply w-auto;
          }
        }
      }
    }
  }
}
</style>
