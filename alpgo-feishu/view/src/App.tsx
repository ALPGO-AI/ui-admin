import React from 'react';
import { useAsync } from 'react-async-hook';
import { Table } from '@douyinfe/semi-ui';
import { getTableData } from './utils';

export const App = () => {
  // 获取 bitable 数据
  const response = useAsync(getTableData, []);

  if (!response.result) return <></>;

  const {
    result: { columns, dataSource },
  } = response;

  return <Table columns={columns} dataSource={dataSource} />;
};
