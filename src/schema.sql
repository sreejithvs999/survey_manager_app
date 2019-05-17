
  
CREATE SEQUENCE answer_option_option_id_seq INCREMENT 1  MINVALUE 0;

CREATE SEQUENCE survey_question_question_id_seq INCREMENT 1  MINVALUE 0;
  
CREATE TABLE survey_question
(
  question_id integer NOT NULL DEFAULT nextval('survey_question_question_id_seq'::regclass),
  question_text character varying(250),
  status integer,
  modified_time timestamp with time zone,
  CONSTRAINT question_pk PRIMARY KEY (question_id)
);
  
CREATE TABLE answer_option
(
  question_id integer NOT NULL,
  description character varying(250),
  vote_count integer,
  option_id integer NOT NULL DEFAULT nextval('answer_option_option_id_seq'::regclass),
  CONSTRAINT options_pk PRIMARY KEY (option_id)
);

CREATE INDEX fki_question_ref  ON public.answer_option  USING btree (question_id);

select setval('answer_option_option_id_seq', 1, false);
select setval('survey_question_question_id_seq', 1, false);


