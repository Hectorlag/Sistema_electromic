<template>
  <div>
    <Sidebar />
  </div>

  <div>
    <NavBar />
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import NavBar from '@/components/NavBar.vue';

export default {
  components: {
    Sidebar,
    NavBar,
  }
}
 
</script>

<style scoped>
</style>
