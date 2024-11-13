## Task Service

### Endpoints
- `POST /tasks` - создание задачи
- `PATCH /tasks/{taskId}` - обновление задачи по `id` (проверка по header - обновлять могут только автор или исполнитель задачи, нельзя обновить `createdDateTime` и `authorId`)
- `GET /tasks/{taskId}` - получение задачи по `id`
- `GET /tasks?page={page}&size={size}&eventId={eventId}&assignTo={assigneeId}&authorId={authorId}` - получение задач с пагинацией и необязательными фильтрами по `id` события, `id` исполнителя и `id` автора
- `DELETE /tasks/{taskId}` - удаление задачи по `id` (проверка по header - удалить может только автор)

### Models
Модель `Task` включает следующие поля: 
- title
- description
- createdDateTime
- deadline
- status
- assigneeId
- authorId
- eventId