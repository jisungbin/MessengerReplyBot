## MessengerReplyBot

`NotificationListenerService`를 기반으로 작동하는 메신저 자동응답 봇.

타입스크립트 코드를 작성하여 메시지 분기를 구성하고, 원하는 답장을 보낼 수 있습니다.

```typescript
function onNewMessage(message: Message) {
  if (message.content === 'hi') {
    message.replier.reply('bye!')
  }
}
```

개인의 취미 활동 목적으로 개발됩니다.

### Engine

> [!TIP]
> 알림에 반응할 서비스와 해당 서비스의 알림을 분석하는 한 단위을 엔진이라 부릅니다.

현재는 카카오톡만 지원하지만, 만약 원한다면 새로운 엔진을 쉽게 추가할 수 있습니다.

새로운 엔진을 추가하는 방법을 다루는 자세한 문서는 곧 작성됩니다.

### License

- MIT
- GPL v3

자세한 내용은 [LICENSE](LICENSE) 파일을 확인해 주세요.
