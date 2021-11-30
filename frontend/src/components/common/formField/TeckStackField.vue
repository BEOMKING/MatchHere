<template>
  <div class="field">
    <div class="input-field">
      <label for="teckstack-input" class="text-sm font-medium">기술스택</label>
      <div class="input-wrapper">
        <input
          type="text"
          id="teckstack-input"
          v-model="searchTerm"
          placeholder="ex) javascript, java, k8s"
        />
        <button class="material-icons" v-if="searchTerm.length === 0">
          search
        </button>
        <button class="material-icons" v-else @click="handleResetSearchTerm">
          close
        </button>
      </div>
    </div>
    <ul class="teckstack-list" v-if="searchTerm">
      <template v-if="searchedTechStackList.length !== 0">
        <li v-for="ts in searchedTechStackList" :key="ts.refIndex">
          <button @click="handleSelectTeckStack(ts.item)">
            <span>{{ ts.item }}</span>
          </button>
        </li>
      </template>
      <li v-else>검색 결과가 없습니다.</li>
    </ul>
  </div>
</template>

<script>
import Fuse from "fuse.js"
import { teckStackList } from "@/libs/data"
import { computed, ref } from "vue"

export default {
  name: "TeckStackField",
  emits: ["SelectTeckStack"],
  setup(_, { emit }) {
    const fuseOptions = {
      threshold: 0.06,
    }
    const teckStackFuse = new Fuse(teckStackList, fuseOptions)
    const searchTerm = ref("")
    const searchedTechStackList = computed(() =>
      teckStackFuse.search(searchTerm.value)
    )

    const handleResetSearchTerm = () => {
      searchTerm.value = ""
    }

    const handleSelectTeckStack = (ts) => {
      emit("SelectTeckStack", ts)
      handleResetSearchTerm()
    }

    return {
      searchTerm,
      searchedTechStackList,
      handleSelectTeckStack,
      handleResetSearchTerm,
    }
  },
}
</script>

<style lang="scss" scoped>
.field {
  @apply relative;

  .input-field {
    @apply grid gap-2;

    label {
      @apply place-self-start;
    }

    .input-wrapper {
      @apply relative;

      input {
        @apply w-full border border-gray-400 rounded-md py-2 px-4 outline-none;

        &:focus {
          @apply ring-1 ring-blue-500;
        }
      }

      .material-icons {
        transform: translateY(-50%);
        @apply absolute top-1/2 right-2;
      }
    }
  }

  .teckstack-list {
    @apply grid gap-2 max-h-40 overflow-scroll absolute left-0 w-full bg-white shadow-md p-2;

    li {
      @apply transition-colors p-2;

      &:hover {
        @apply bg-gray-100;
      }

      button {
        @apply w-full text-left;
      }
    }
  }
}
</style>
