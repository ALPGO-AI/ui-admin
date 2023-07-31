
import { testAction, createActionContext } from '@lark-opdev/block-basekit-server-api';

async function test() {
    const actionContext = await createActionContext({
      tenantAccessToken: '',
    });

    testAction({
        text: 'hello world',
        transformType: 'generateFontArt',
    },
    actionContext);
}

test();
