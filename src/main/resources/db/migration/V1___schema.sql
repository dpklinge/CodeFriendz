CREATE TABLE code_friendz_app_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
    display_name TEXT NOT NULL,
    password TEXT NOT NULL,
    email TEXT NOT NULL,
    phone_number TEXT NOT NULL
);



CREATE TABLE user_project(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
    owner UUID REFERENCES code_friendz_app_user(id),
    name TEXT UNIQUE NOT NULL,
    description TEXT NOT NULL,
    repo_url TEXT,
    project_image bytea,
    message_thread_id UUID
);

CREATE TABLE user_project_members(
    user_id UUID REFERENCES code_friendz_app_user(id),
    project_id UUID REFERENCES user_project(id),
    PRIMARY KEY(user_id, project_id)
);



CREATE TABLE message(
    thread_id UUID,
    author_id UUID REFERENCES code_friendz_app_user(id),
    contents TEXT NOT NULL,
    sent_at TIMESTAMP,
    is_edited BOOLEAN,
    PRIMARY KEY(thread_id, sent_at)
);
CREATE INDEX thread_index ON message(thread_id);
