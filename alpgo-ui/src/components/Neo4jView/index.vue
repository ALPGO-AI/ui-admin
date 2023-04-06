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
        return nodes
          .filter(node => {
            const n = node.n;
            const properties = n.properties;
            if (n.labels[0] === "Tag") {
              return false;
            }
            return true;
          })
          .map((node) => {
          const n = node.n;
          const properties = n.properties;
          return {
            id: n.id,
            label: properties.name?.val || properties.patternStyle?.val,
            properties,
            group: n.labels[0],
            image: properties.outputImageUrls?.val && JSON.parse(properties.outputImageUrls.val)[0] + "?imageMogr2/thumbnail/!25p",
            imageSrc: properties.outputImageUrls?.val && JSON.parse(properties.outputImageUrls.val)[0],
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
            // label: r.type + r.start + r.end,
            properties, 
          };
        });
      };
      const formattedNodes = formatNodes(this.nodes);
      const nodes = new vis.DataSet(formattedNodes);
      const edges = new vis.DataSet(formatRelations(this.relations));
      const nodeMap = {};
      for (let index = 0; index < formattedNodes.length; index++) {
        const node = formattedNodes[index];
        nodeMap[node.id] = node;
      }
      // create a network
      var container = document.getElementById("mynetwork");
      var data = {
        nodes: nodes,
        edges: edges
      };
      var options = {
        nodes: {
          shape: "dot",
          size: 16,
        },
        physics: {
          forceAtlas2Based: {
            gravitationalConstant: -26,
            centralGravity: 0.005,
            springLength: 230,
            springConstant: 0.18,
          },
          maxVelocity: 146,
          solver: "forceAtlas2Based",
          timestep: 0.35,
          stabilization: { iterations: 150 },
        },
        groups: {
          Output: {
            shape: "image",
            size: 8,
          },
        },
      };
      var network = new vis.Network(container, data, options);
      const that = this;
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
