<script>
export default {
  data () {
    return {
      title: 'The header from data',
      number: 100
    }
  }
}
</script>

<template>
  <h2>{{ this.title }}</h2>
  <h3>{{ number }}</h3>
</template>

<style scoped>

</style>
