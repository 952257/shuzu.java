<template>
  <div class="app-shell" @pointermove="onMove">
    <div class="liquid-bg" aria-hidden="true">
      <span class="blob blob-a"></span>
      <span class="blob blob-b"></span>
      <span class="blob blob-c"></span>
      <span class="blob blob-d"></span>
      <span class="blob blob-e"></span>
      <span class="caustic"></span>
    </div>
    <div class="env-light" aria-hidden="true"></div>
    <svg class="lg-svg" aria-hidden="true">
      <filter id="lg-edge" x="-20%" y="-20%" width="140%" height="140%" color-interpolation-filters="sRGB">
        <feMorphology in="SourceAlpha" operator="erode" radius="7" result="inner" />
        <feComposite in="SourceAlpha" in2="inner" operator="out" result="rim" />
        <feGaussianBlur in="rim" stdDeviation="3.2" result="softRim" />
        <feColorMatrix in="softRim" type="matrix" values="0 0 0 0.5 0.5  0 0 0 0.28 0.5  0 0 0 0 0.5  0 0 0 0 1" result="map" />
        <feDisplacementMap in="SourceGraphic" in2="map" scale="18" xChannelSelector="R" yChannelSelector="G" />
      </filter>
    </svg>
    <router-view />
  </div>
</template>

<script setup>
const onMove = (e) => {
  document.documentElement.style.setProperty("--lg-x", `${e.clientX}px`);
  document.documentElement.style.setProperty("--lg-y", `${e.clientY}px`);
};
</script>
