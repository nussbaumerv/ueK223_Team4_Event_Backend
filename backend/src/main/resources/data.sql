--USERS
insert into users (id, email, first_name, last_name, password)
values ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'admin@example.com', 'James', 'Bond',
        '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'), -- Password: 1234
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'user@example.com', 'Tyler', 'Durden',
        '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),  -- Password: 1234
       ('f9ed81db-f90a-42d4-b7e4-d554d8f338fd', 'nussbaumerv9@gmail.com', 'Valentin', 'Nussbaumer',
        '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6')  -- Password: 1234
ON CONFLICT DO NOTHING;


--ROLES
INSERT INTO role(id, name)
VALUES ('d29e709c-0ff1-4f4c-a7ef-09f656c390f1', 'DEFAULT'),
       ('ab505c92-7280-49fd-a7de-258e618df074', 'ADMIN'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', 'USER')
ON CONFLICT DO NOTHING;

--AUTHORITIES
INSERT INTO authority(id, name)
VALUES ('2ebf301e-6c61-4076-98e3-2a38b31daf86', 'DEFAULT'),
       ('76d2cbf6-5845-470e-ad5f-2edb9e09a868', 'USER_MODIFY'),
       ('21c942db-a275-43f8-bdd6-d048c21bf5ab', 'USER_DELETE'),
       ('b7bf613b-6619-4a72-abb6-5dc6734b9ae3', 'ADMIN_READ'),
       ('5c6d5948-2c06-487c-bd3e-9ae23ded960b', 'ADMIN_CREATE'),
       ('3b77df60-67df-4071-ac29-abbd451da1c5', 'ADMIN_MODIFY'),
       ('e5a448e3-feaf-4535-9ac0-92b1471de468', 'ADMIN_DELETE'),
       ('d9b3a36d-4a52-4b08-8415-959a544b2996', 'PARTICIPATE_EVENT')
ON CONFLICT DO NOTHING;

--assign roles to users
insert into users_role (users_id, role_id)
values ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'd29e709c-0ff1-4f4c-a7ef-09f656c390f1'),
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'd29e709c-0ff1-4f4c-a7ef-09f656c390f1'),
       ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'ab505c92-7280-49fd-a7de-258e618df074'),
       ('f9ed81db-f90a-42d4-b7e4-d554d8f338fd', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('f9ed81db-f90a-42d4-b7e4-d554d8f338fd', 'd29e709c-0ff1-4f4c-a7ef-09f656c390f1')
ON CONFLICT DO NOTHING;

--assign authorities to roles
INSERT INTO role_authority(role_id, authority_id)
VALUES ('d29e709c-0ff1-4f4c-a7ef-09f656c390f1', '2ebf301e-6c61-4076-98e3-2a38b31daf86'),
       ('ab505c92-7280-49fd-a7de-258e618df074', '76d2cbf6-5845-470e-ad5f-2edb9e09a868'),
       ('ab505c92-7280-49fd-a7de-258e618df074', '21c942db-a275-43f8-bdd6-d048c21bf5ab'),
       ('ab505c92-7280-49fd-a7de-258e618df074', '3b77df60-67df-4071-ac29-abbd451da1c5'),
       ('ab505c92-7280-49fd-a7de-258e618df074', 'e5a448e3-feaf-4535-9ac0-92b1471de468'),
       ('ab505c92-7280-49fd-a7de-258e618df074', 'b7bf613b-6619-4a72-abb6-5dc6734b9ae3'),
       ('ab505c92-7280-49fd-a7de-258e618df074', '5c6d5948-2c06-487c-bd3e-9ae23ded960b'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', '21c942db-a275-43f8-bdd6-d048c21bf5ab'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', 'd9b3a36d-4a52-4b08-8415-959a544b2996'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', '76d2cbf6-5845-470e-ad5f-2edb9e09a868'),
       ('d29e709c-0ff1-4f4c-a7ef-09f656c390f1', 'd9b3a36d-4a52-4b08-8415-959a544b2996')
ON CONFLICT DO NOTHING;

--Events
INSERT INTO event (id, name, date, location, description, owner_id)
VALUES ('af7c1fe6-d669-414e-b066-e9733f0de7a8', 'Openair Frauenfeld', '11. - 13. July 2024', 'Frauenfeld',
        'Das Openair Frauenfeld ist das groesste Open-Air-Musikfestival der Deutschschweiz und das groesste Hip-Hop-Festival Europas. ',
        '0d8fa44c-54fd-4cd0-ace9-2a7da57992de'),
       ('ff68e22e-633f-4fe3-b482-590c7163b7e1', 'Zuerich Openair', ' 23. - 31. August 2024', 'Zuerich',
        'Das Zuerich Openair ist ein Musikfestival in der Nähe des Flughafens Zuerich auf dem Gelaende der Gemeinden Ruemlang und Opfikon.',
        'ba804cb9-fa14-42a5-afaf-be488742fc54') ON CONFLICT DO NOTHING;

--assign guests to events
INSERT INTO event_guests(event_id, guests_id)
VALUES ('af7c1fe6-d669-414e-b066-e9733f0de7a8', 'f9ed81db-f90a-42d4-b7e4-d554d8f338fd')
ON CONFLICT DO NOTHING;