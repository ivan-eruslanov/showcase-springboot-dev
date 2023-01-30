create table t_task(
    id uuid primary key,
    c_details text check ( length(c_details) > 0 ),
    c_completed boolean not null default false
);