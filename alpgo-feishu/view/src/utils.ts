import {
  bitable,
  IOpenCellValue,
  ViewType,
} from '@lark-opdev/block-bitable-api';
import { getRenderFunc } from './render_helper';

export const getTableData = async () => {
  // 获取 selection
  const selection = await bitable.base.getSelection();
  if (!selection.tableId) return null;
  // 获取当前表
  const table = await bitable.base.getTableById(selection.tableId);
  // 获取视图列表，并取出第一个 grid 视图 meta
  const views = await table.getViewMetaList();
  const GridViewMeta = views.find((view) => view.type === ViewType.Grid);
  const view = await table.getViewById(GridViewMeta!.id);
  // 获取视图下的 records & fields
  const records = await view.getVisibleRecordIdList();
  const fieldMetas = await view.getFieldMetaList();
  // 获取每个单元格的值
  const recordValues = await Promise.all(
    records.filter(Boolean).map(async (recordId) => {
      if (!recordId) return;
      const recordValue: Record<string, IOpenCellValue> = {};
      await Promise.all(
        fieldMetas.map(async (fieldMeta) => {
          const cellValue = await table.getCellValue(fieldMeta.id, recordId);
          recordValue[fieldMeta.id] = cellValue;
          return cellValue;
        })
      );
      recordValue['recordId'] = recordId;
      return recordValue;
    })
  );
  // 生成 表头配置
  const columns = fieldMetas.map((meta) => {
    return {
      title: meta.name,
      dataIndex: meta.id,
      // 获取渲染函数
      render: getRenderFunc({ meta, table }),
    };
  });
  // 生成表数据
  const dataSource = recordValues.filter(Boolean) as Record<
    string,
    IOpenCellValue
  >[];
  return {
    table,
    columns,
    dataSource,
  };
};
