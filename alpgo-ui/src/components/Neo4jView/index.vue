<template>
  <div class="neo4j-view">
    <div id="mynetwork"></div>
  </div>
</template>

<script>
require("vis-network/dist/dist/vis-network.min.css");
const vis = require("vis-network/dist/vis-network.min");
export default {
  name: "Neo4jView",
  components: {},
  props: {
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
      // TBD: format nodes method use props
      const formatNodes = (nodes) => {
        return nodes.map((node) => {
          const n = node.n;
          const properties = n.properties;
          return {
            id: n.id,
            label: properties.name?.val || properties.patternStyle?.val,
            properties,
            group: n.labels[0],
            image: properties.outputImageUrls?.val && JSON.parse(properties.outputImageUrls.val)[0] + "?imageMogr2/thumbnail/!25p",
          };
        });
      };
      const formatRelations = (relations) => {
        return relations.map((relation) => {
          const r = relation.r;
          const properties = r.properties;
          return {
            from: r.start,
            to: r.end,
            // label: r.type,
            // properties, 
          };
        });
      };
      const nodes = new vis.DataSet(formatNodes(this.nodes));
      const edges = new vis.DataSet(formatRelations(this.relations));

      // create a network
      var container = document.getElementById("mynetwork");
      var data = {
        nodes: nodes,
        edges: edges
      };
      var options = {
        autoResize: true,
        edges: {
          physics: false,
        },
        groups: {
          useDefaultGroups: true,
          myGroupId: {},
          Pattern: {
            shape: "circle",
            color: { background: "#fd91b7" },
            font: { color: "white" },
          },
          Output: {
            shape: "image",
            color: { background: "#7ed6df" },
          },
          Tag: {
            shape: "circle",
            color: { background: "#7ed6df" },
          },
        },
      };
      var network = new vis.Network(container, data, options);
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
