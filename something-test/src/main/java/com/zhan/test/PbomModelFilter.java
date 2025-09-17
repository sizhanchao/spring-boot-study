package com.zhan.test;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhan
 * @since 2025-09-17 22:09
 */
public class PbomModelFilter {

    public static class PbomModel {
        private String itemCode;
        private String parentItemCode;
        private String productLevel;

        public PbomModel(String itemCode, String parentItemCode, String productLevel) {
            this.itemCode = itemCode;
            this.parentItemCode = parentItemCode;
            this.productLevel = productLevel;
        }

        public String getItemCode() {
            return itemCode;
        }

        public String getParentItemCode() {
            return parentItemCode;
        }

        public String getProductLevel() {
            return productLevel;
        }

        @Override
        public String toString() {
            return "PbomModel{itemCode='" + itemCode + "', parentItemCode='" + parentItemCode + "', productLevel='" + productLevel + "'}";
        }
    }

    /**
     * 过滤PbomModel列表，返回满足条件的元素
     * 条件：当前对象的productLevel以"w"开头，或者它的子集中有以"w"开头的
     * @param pbomList 原始PbomModel列表
     * @return 过滤后的列表
     */
    public static List<PbomModel> filterPbomModels(List<PbomModel> pbomList) {
        // 构建父子关系映射表 (父节点ID -> 子节点列表)
        Map<String, List<PbomModel>> parentChildrenMap = pbomList.stream()
                .filter(model -> model.getParentItemCode() != null && !model.getParentItemCode().isEmpty())
                .collect(Collectors.groupingBy(PbomModel::getParentItemCode));

        // 使用Stream API过滤：检查当前节点或子节点是否有以"w"开头的productLevel
        return pbomList.stream()
                .filter(model -> hasWInHierarchy(model, parentChildrenMap, new HashSet<>()))
                .collect(Collectors.toList());
    }

    /**
     * 递归检查当前节点或任何子节点的productLevel是否以"w"开头
     * @param model 当前节点
     * @param parentChildrenMap 父子关系映射表
     * @param visited 已访问节点集合，用于检测循环引用
     * @return 如果当前节点或任何子节点满足条件返回true，否则返回false
     */
    private static boolean hasWInHierarchy(PbomModel model,
                                           Map<String, List<PbomModel>> parentChildrenMap,
                                           Set<String> visited) {
        String itemCode = model.getItemCode();

        // 防止循环引用
        if (visited.contains(itemCode)) {
            return false;
        }
        visited.add(itemCode);

        // 检查当前节点
        if (model.getProductLevel() != null && model.getProductLevel().toLowerCase().startsWith("w")) {
            return true;
        }

        // 检查所有子节点
        List<PbomModel> children = parentChildrenMap.getOrDefault(itemCode, Collections.emptyList());
        for (PbomModel child : children) {
            if (hasWInHierarchy(child, parentChildrenMap, visited)) {
                return true;
            }
        }

        return false;
    }

    // 测试代码
    public static void main(String[] args) {
        // 创建测试数据，基于您提供的文件内容
        List<PbomModel> pbomList = Arrays.asList(
                new PbomModel("KZ-MOD-001", null, "W"), // 控制模块
                new PbomModel("KZ-PCB-001", "KZ-MOD-001", "W"), // 电路板
                new PbomModel("KZ-PCB-001-0", "KZ-PCB-001", "W"), // 主控芯片
                new PbomModel("F1B0-0", "F117", ""), // 燃料换向阀
                new PbomModel("F1B21-0", "F1B0-0", ""), // 煤油预压涡轮泵
                new PbomModel("6204TF_HVP5CU", "F1B21-0", ""), // 轴承
                new PbomModel("6206TF_HVP5CU", "F1B21-0", "W"), // 轴承（有W）
                new PbomModel("6206TF_HVP5CU-1", "6206TF_HVP5CU", "W"),
                new PbomModel("6206TF_HVP5CU-2", "6206TF_HVP5CU", "W"),
                new PbomModel("6205TF_HVP5CU", "F1B21-0", "") // 轴承-1
        );

        System.out.println("原始列表:");
        pbomList.forEach(System.out::println);

        System.out.println("\n过滤后的列表（满足条件的节点）:");
        List<PbomModel> filteredList = filterPbomModels(pbomList);
        filteredList.forEach(System.out::println);

        // 验证特定节点是否在结果中
        System.out.println("\n验证特定节点:");
        boolean has6204 = filteredList.stream()
                .anyMatch(model -> model.getItemCode().equals("6204TF_HVP5CU"));
        boolean has6205 = filteredList.stream()
                .anyMatch(model -> model.getItemCode().equals("6205TF_HVP5CU"));
        boolean has6206 = filteredList.stream()
                .anyMatch(model -> model.getItemCode().equals("6206TF_HVP5CU"));

        System.out.println("包含6204TF_HVP5CU: " + has6204);
        System.out.println("包含6205TF_HVP5CU: " + has6205);
        System.out.println("包含6206TF_HVP5CU: " + has6206);
    }
}
