insert into t_application_user(id, c_username, c_password)
values ('2bf980b0-a09c-11ed-af03-00155d3eaee0', 'user1', '{noop}password1'),
       ('2e1db6a4-a09c-11ed-8df3-00155d3eaee0', 'user2', '{noop}password2');

insert into t_task(id, c_details, c_completed, id_application_user)
values ('96ef3ea2-9ef1-11ed-ad46-00155d174057', 'first task', false, '2bf980b0-a09c-11ed-af03-00155d3eaee0'),
       ('9946a352-9ef1-11ed-9c87-00155d174057', 'second task', true, '2bf980b0-a09c-11ed-af03-00155d3eaee0'),
       ('a50f95d6-a09f-11ed-bf6e-00155d3eaee0', 'third task', false, '2e1db6a4-a09c-11ed-8df3-00155d3eaee0');