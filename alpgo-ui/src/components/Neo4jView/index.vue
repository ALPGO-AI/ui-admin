<template>
  <div class="neo4j-view">
    <div id="mynetwork"></div>
    <div class="menu" id="divHoverNode" style="display: none;" />
  </div>
</template>

<script>
require("vis-network/dist/dist/vis-network.min.css");
const vis = require("vis-network/dist/vis-network.min");
export default {
  name: "Neo4jView",
  components: {},
  props: {
    doubleClickNode: {
      type: Function,
      default: () => {},
    },
    nodes: {
      type: Array,
      default: () => [],
    },
    relations: {
      type: Array,
      default: () => [],
    },
  },
  mounted() {
  },
  data() {
    return {
      index: null,
    };
  },
  methods: {
    redraw() {
      const nodes = new vis.DataSet(this.nodes);
      const edges = new vis.DataSet(this.relations);
      // create a network
      var container = document.getElementById("mynetwork");
      var data = {
        nodes: nodes,
        edges: edges
      };
      var options = {
        nodes: {
          borderWidth: 0,
          borderWidthSelected: 1,
          size: 32,
          color: {
            border: "white",
            background: "black",
            highlight: {
              border: "black",
              background: "white",
            },
            hover: {
              border: "orange",
              background: "grey",
            },
          },
          font: { color: "red" },
          shapeProperties: {
            useBorderWithImage: true,
          },
        },
        edges: {
          color: "lightgray",
        },
      };
      var network = new vis.Network(container, data, options);
      network.on("doubleClick", this.doubleClickNode);
    }
  },
};
</script>

<style scoped>
.neo4j-view {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}
#mynetwork {
  width: 100%;
  height: 600px;
  border: 1px solid lightgray;
}
</style>
