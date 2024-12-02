## Task Service

### Endpoints

#### Задачи:
- `POST /tasks` - создание задачи
- `PATCH /tasks/{taskId}` - обновление задачи по `id` (проверка по header - обновлять могут только автор или исполнитель задачи, нельзя обновить `createdDateTime` и `authorId`)
- `GET /tasks/{taskId}` - получение задачи по `id`
- `GET /tasks?page={page}&size={size}&eventId={eventId}&assignTo={assigneeId}&authorId={authorId}` - получение задач с пагинацией и необязательными фильтрами по `id` события, `id` исполнителя и `id` автора
- `DELETE /tasks/{taskId}` - удаление задачи по `id` (проверка по header - удалить может только автор)

#### Эпики (группы задач):
- `POST /tasks/epics` - создание эпика
- `PATCH /tasks/epics/{epicId}` - обновление эпика по `id` (проверка по header - обновлять может только ответственный эпика, нельзя обновить `eventId`)
- `POST /tasks/epics/{epicId}?taskIds=taskIds` - добавление задач в эпик (проверка по header - добавлять задачи может только ответственный эпика,
задача может быть только с eventId этого эпика)
- `GET /tasks/epics/{epicId}` - получение эпика по `id`
- `DELETE /tasks/epics/{epicId}` - удаление эпика по `id` (проверка по header - удалить может только ответственный)

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

Модель `Epic` включает следующие поля:
- name
- ownerId
- eventId
- deadline
- tasks