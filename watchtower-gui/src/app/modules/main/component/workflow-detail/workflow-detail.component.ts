import { Component, OnInit } from '@angular/core';
import {Workflow} from "../../entity/workflow/workflow";
import {WorkflowService} from "../../service/workflow.service";
import {ActivatedRoute} from "@angular/router";
import {ParamMap} from "@angular/router/src/shared";

@Component({
  selector: 'wt-workflow-detail',
  templateUrl: './workflow-detail.component.html',
  styleUrls: ['./workflow-detail.component.scss']
})
export class WorkflowDetailComponent implements OnInit {

  workflow: Workflow;

  constructor(private workflowService: WorkflowService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
        console.log('Getting params');
        const workflowId: string = params.get('id');
        this.fetchWorkflow(workflowId);
      }
    );
  }

  fetchWorkflow(workflowId: string | number): void {
    console.log(`Fetching workflow ${workflowId}`);

    this.workflowService.getWorkflow(workflowId).subscribe((workflow: Workflow) => this.workflow = workflow)
  }

}